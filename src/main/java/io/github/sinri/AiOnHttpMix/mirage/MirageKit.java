package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTKit;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.provider.volces.VolcesKit;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.github.sinri.AiOnHttpMix.utils.specification.DashscopeModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageKit {
    private final String mirageDomain;
    private final String clientCode;
    private final String clientSecret;

    private MirageKit(String mirageDomain, String clientCode, String clientSecret) {
        this.mirageDomain = mirageDomain;
        this.clientCode = clientCode;
        this.clientSecret = clientSecret;
    }

    public MirageKit(MirageConfigElement configElement) {
        this(configElement.getDomain(), configElement.getClientCode(), configElement.getClientSecret());
    }

    public String getMirageDomain() {
        return mirageDomain;
    }

    protected JsonObject buildRequestBody(boolean useNyaCode, MixChatRequest mixChatRequest) {
        return buildRequestBody(useNyaCode, mixChatRequest.toJsonObject());
    }

    /**
     * @param useNyaCode 传输内容是否使用NyaCode编码绕过防火墙。
     */
    private JsonObject buildRequestBody(boolean useNyaCode, JsonObject rawMixChatRequest) {
        var timestamp = System.currentTimeMillis();
        String checksum = Keel.digestHelper().md5(clientCode + "@" + timestamp + "@" + clientSecret);

        JsonObject body = new JsonObject()
                .put("client_code", clientCode)
                .put("timestamp", timestamp)
                .put("checksum", checksum);

        body.put("use_nyacode", useNyaCode);
        if (useNyaCode) {
            body.put("request", Keel.stringHelper().encodeToNyaCode(rawMixChatRequest.toString()));
        } else {
            body.put("request", rawMixChatRequest);
        }
        return body;
    }

    public Future<MixChatResponse> requestSync(boolean useNyaCode, MixChatRequest mixChatRequest) {
        var body = buildRequestBody(useNyaCode, mixChatRequest);
        return Keel.useWebClient(webClient -> {
            var url = "https://" + getMirageDomain() + "/mirage/aigc/mix/sync";

            AigcMix.getVerboseLogger().info("MirageSDK.requestSync POST " + url + "\n" + body);

            return webClient.postAbs(url)
                            .sendJsonObject(body)
                            .compose(bufferHttpResponse -> {
                                if (bufferHttpResponse.statusCode() != 200) {
                                    return Future.failedFuture(new Exception("Status Code:" + bufferHttpResponse.statusCode() + "; " + bufferHttpResponse.bodyAsString()));
                                }

                                AigcMix.getVerboseLogger()
                                       .info("MirageSDK.requestSync::bufferHttpResponse | " + bufferHttpResponse.bodyAsString());

                                var resp = bufferHttpResponse.bodyAsJsonObject();
                                MixChatResponse mixChatResponse = MixChatResponse.wrap(resp.getJsonObject("data"));
                                return Future.succeededFuture(mixChatResponse);
                            });
        });
    }

    public Future<Void> requestStream(boolean useNyaCode, MixChatRequest mixChatRequest, Function<String, Future<Void>> func) {
        var body = buildRequestBody(useNyaCode, mixChatRequest);

        ChatModel chatModel = mixChatRequest.getChatModel();

        Function<String, Future<JsonObject>> transformer;
        if (chatModel instanceof GPTModelSpecification) {
            transformer = fragment -> {
                return GPTKit.parseStreamFragmentToChunk(fragment)
                             .compose(chunk -> {
                                 return Future.succeededFuture(chunk.cloneAsJsonObject());
                             });
            };
        } else if (chatModel instanceof VolcesModelSpecification) {
            transformer = fragment -> {
                return VolcesKit.parseStreamFragmentToChunk(fragment)
                                .compose(chunk -> {
                                    return Future.succeededFuture(chunk.cloneAsJsonObject());
                                });
            };
        } else if (chatModel instanceof DashscopeModelSpecification) {
            transformer = fragment -> {
                return QwenKit.parseStreamFragmentToChunk(fragment)
                              .compose(chunk -> {
                                  return Future.succeededFuture(chunk.cloneAsJsonObject());
                              });
            };
        } else {
            throw new IllegalArgumentException("model is not supported");
        }


        return ServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(getMirageDomain())
                        .setDefaultPort(443),
                client -> client
                        .request(HttpMethod.POST, "/mirage/aigc/mix/stream")
                        .compose(httpClientRequest -> {
                            httpClientRequest.putHeader("Content-Type", "application/json");
                            return httpClientRequest
                                    .send(body.toString());
                        }),
                fragment -> {
                    return transformer.apply(fragment)
                                      .compose(jsonObject -> {
                                          return func.apply(jsonObject.toString());
                                      });
                },
                mixChatRequest.getTimeout()
        );
    }
}
