package io.github.sinri.AiOnHttpMix.mirage.vl;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mirage.MirageSDKCore;
import io.github.sinri.AiOnHttpMix.mirage.chat.MirageServiceEnum;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMRequest;
import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMResponse;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.keel.core.cutter.IntravenouslyCutter;
import io.github.sinri.keel.core.cutter.IntravenouslyCutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageVLSDK extends MirageSDKCore {
    public MirageVLSDK(String mirageDomain, String clientCode, String clientSecret) {
        super(mirageDomain, clientCode, clientSecret);
    }

    /**
     * @since 1.2.6
     */
    public Future<AnyVLLMResponse> requestSync(
            MirageServiceEnum mirageService,
            boolean useNyaCode,
            MirageVLRequestEntity vllmRequestBody
    ) {
        return requestSync(
                mirageService.getMappedSupportedModel().name(),
                mirageService.getServiceCode(),
                useNyaCode,
                vllmRequestBody
        );
    }

    public Future<AnyVLLMResponse> requestSync(
            String model,
            String service,
            boolean useNyaCode,
            MirageVLRequestEntity vllmRequestBody
    ) {
        var body = buildRequestBody(model, service, useNyaCode, vllmRequestBody);
        return Keel.useWebClient(webClient -> {
            var url = "https://" + getMirageDomain() + "/mirage/aigc/vl/sync";

            AigcMix.getVerboseLogger()
                   .debug("io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLSDK.requestSync URL: " + url);
            AigcMix.getVerboseLogger()
                   .debug("io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLSDK.requestSync BODY", body);

            return webClient.postAbs(url)
                            .sendJsonObject(body)
                            .compose(bufferHttpResponse -> {
                                if (bufferHttpResponse.statusCode() != 200) {
                                    return Future.failedFuture(new Exception("Status Code:" + bufferHttpResponse.statusCode() + "; " + bufferHttpResponse.bodyAsString()));
                                }

                                AigcMix.getVerboseLogger()
                                       .debug("io.github.sinri.AiOnHttpMix.mirage.chat.MirageSDK.requestSync::bufferHttpResponse", bufferHttpResponse.bodyAsJsonObject());

                                var resp = bufferHttpResponse.bodyAsJsonObject();
                                MirageSyncVLResponse mirageSyncVLResponse = new MirageSyncVLResponse(resp);
                                AnyVLLMResponse anyVLLMResponse = mirageSyncVLResponse.toAnyVLLMResponse();
                                return Future.succeededFuture(anyVLLMResponse);
                            });
        });
    }

    /**
     * @param fragmentHandler 针对一个已经格式化好的SSE Chunk的JSON对象字符串表达的处理器
     * @see AnyLLMKit#request(AnyLLMRequest, Handler)
     */
    public Future<Void> requestStream(
            String model,
            String service,
            boolean useNyaCode,
            MirageVLRequestEntity vllmRequestBody,
            long maxStreamTime,
            Handler<String> fragmentHandler
    ) {
        var body = buildRequestBody(model, service, useNyaCode, vllmRequestBody);

        IntravenouslyCutter<String> cutter = new IntravenouslyCutterOnString(s -> {
            AigcMix.getVerboseLogger().debug(
                    "io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLSDK.requestStream::IntravenouslyCutter Drop | " + s);

            //Keel.getLogger().fatal("MirageSDK.requestStream cut off: " + s);
            var lines = s.split("[\r\n]+");
            for (var line : lines) {
                if (line.startsWith("data:")) {
                    line = line.replaceAll("^data:\\s*", "");
                    AigcMix.getVerboseLogger()
                           .debug("io.github.sinri.AiOnHttpMix.mirage.vl.MirageVLSDK.requestStream::line | " + line);
                    line = line.replaceFirst("id:\\d+", "");
                    if (!line.isEmpty()) {
                        fragmentHandler.handle(line);
                    }
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
                client -> {
                    return client
                            .request(HttpMethod.POST, "/mirage/aigc/vl/stream")
                            .compose(httpClientRequest -> {
                                httpClientRequest.putHeader("Content-Type", "application/json");
                                return httpClientRequest
                                        .send(body.toString());
                            });
                },
                cutter,
                maxStreamTime,
                "Undefined"
        );
    }
}
