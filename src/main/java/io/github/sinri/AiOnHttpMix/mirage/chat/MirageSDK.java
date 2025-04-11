package io.github.sinri.AiOnHttpMix.mirage.chat;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mirage.MirageSDKCore;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMResponse;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.keel.core.cutter.IntravenouslyCutter;
import io.github.sinri.keel.core.cutter.IntravenouslyCutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageSDK extends MirageSDKCore {


    public MirageSDK(String mirageDomain, String clientCode, String clientSecret) {
        super(mirageDomain, clientCode, clientSecret);
    }

    /**
     * @since 1.2.6
     */
    public Future<AnyLLMResponse> requestSync(
            MirageServiceEnum mirageService,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody
    ) {
        return requestSync(
                mirageService.getMappedSupportedModel().name(),
                mirageService.getServiceCode(),
                useNyaCode,
                llmRequestBody
        );
    }

    public Future<AnyLLMResponse> requestSync(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody
    ) {
        var body = buildRequestBody(model, service, useNyaCode, llmRequestBody);
        return Keel.useWebClient(webClient -> {
            var url = "https://" + getMirageDomain() + "/mirage/aigc/llm/sync";
            return webClient.postAbs(url)
                            .sendJsonObject(body)
                            .compose(bufferHttpResponse -> {
                                if (bufferHttpResponse.statusCode() != 200) {
                                    return Future.failedFuture(new Exception("Status Code:" + bufferHttpResponse.statusCode() + "; " + bufferHttpResponse.bodyAsString()));
                                }

                                AigcMix.getVerboseLogger()
                                       .debug("io.github.sinri.AiOnHttpMix.mirage.chat.MirageSDK.requestSync::bufferHttpResponse | " + bufferHttpResponse.bodyAsString());

                                var resp = bufferHttpResponse.bodyAsJsonObject();
                                MirageSyncResponse mirageSyncResponse = new MirageSyncResponse(resp);
                                AnyLLMResponse anyLLMResponse = mirageSyncResponse.toAnyLLMResponse();
                                return Future.succeededFuture(anyLLMResponse);
                            });
        });
    }

    /**
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @since 1.2.6
     */
    public Future<Void> requestStream(
            MirageServiceEnum mirageService,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody,
            long maxStreamTime,
            Handler<String> fragmentHandler
    ) {
        return requestStream(
                mirageService.getMappedSupportedModel().name(),
                mirageService.getServiceCode(),
                useNyaCode,
                llmRequestBody,
                maxStreamTime,
                fragmentHandler
        );
    }

    /**
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @see AnyLLMKit#request(AnyLLMRequest, Handler)
     */
    public Future<Void> requestStream(
            String model,
            String service,
            boolean useNyaCode,
            MirageRequestEntity llmRequestBody,
            long maxStreamTime,
            Handler<String> fragmentHandler
    ) {
        var body = buildRequestBody(model, service, useNyaCode, llmRequestBody);

        IntravenouslyCutter<String> cutter = new IntravenouslyCutterOnString(s -> {
            AigcMix.getVerboseLogger().debug(
                    "io.github.sinri.AiOnHttpMix.mirage.chat.MirageSDK.requestStream::IntravenouslyCutter Drop | " + s);

            //Keel.getLogger().fatal("MirageSDK.requestStream cut off: " + s);
            var lines = s.split("[\r\n]+");
            for (var line : lines) {
                if (line.startsWith("data:")) {
                    line = line.replaceAll("^data:\\s*", "");
                    AigcMix.getVerboseLogger()
                           .debug("io.github.sinri.AiOnHttpMix.mirage.chat.MirageSDK.requestStream::line | " + line);

                    fragmentHandler.handle(line);
                    break;
                }
            }

            return Future.succeededFuture();
        });

        return ServiceMeta.requestSSEImpl(
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
                cutter,
                maxStreamTime * 1000L,
                "Undefined"
        );
    }
}
