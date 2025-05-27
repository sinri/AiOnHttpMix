package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.chat.response.MixChatResponse;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
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

    public MirageKit(String mirageDomain, String clientCode, String clientSecret) {
        this.mirageDomain = mirageDomain;
        this.clientCode = clientCode;
        this.clientSecret = clientSecret;
    }

    public String getMirageDomain() {
        return mirageDomain;
    }

    protected JsonObject buildRequestBody(String model, boolean useNyaCode, MirageRequestEntity llmRequestBody) {
        return buildRequestBody(
                model,
                useNyaCode,
                llmRequestBody.toJsonObject()
        );
    }


    /**
     * @param model      模型的定义，一般约定使用 {@link io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum} 。
     * @param useNyaCode 传输内容是否使用NyaCode编码绕过防火墙。
     */
    private JsonObject buildRequestBody(String model, boolean useNyaCode, JsonObject llmRequestBody) {
        var timestamp = System.currentTimeMillis();
        String checksum = Keel.digestHelper().md5(clientCode + "@" + timestamp + "@" + clientSecret);

        JsonObject body = new JsonObject()
                .put("client_code", clientCode)
                .put("timestamp", timestamp)
                .put("checksum", checksum);

        body.put("model", model);
        body.put("use_nyacode", useNyaCode);
        if (useNyaCode) {
            body.put("request", Keel.stringHelper().encodeToNyaCode(llmRequestBody.toString()));
        } else {
            body.put("request", llmRequestBody);
        }
        return body;
    }

    public Future<MixChatResponse> requestSync(String model, boolean useNyaCode, MirageRequestEntity llmRequestBody) {
        var body = buildRequestBody(
                model,
                useNyaCode,
                llmRequestBody
        );
        return Keel.useWebClient(webClient -> {
            var url = "https://" + getMirageDomain() + "/mirage/aigc/llm/sync";

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

    public Future<Void> requestStream(String model, boolean useNyaCode, MirageRequestEntity llmRequestBody, Function<String, Future<Void>> func) {
        var body = buildRequestBody(
                model,
                useNyaCode,
                llmRequestBody
        );
        return ServiceAdapter.callStreamWithCutter(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(getMirageDomain())
                        .setDefaultPort(443),
                client -> client
                        .request(HttpMethod.POST, "/mirage/aigc/llm/stream")
                        .compose(httpClientRequest -> {
                            httpClientRequest.putHeader("Content-Type", "application/json");
                            return httpClientRequest
                                    .send(body.toString());
                        }),
                func,
                llmRequestBody.getMaxExecutionSeconds() * 1000L
        );
    }
}
