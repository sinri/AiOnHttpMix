package io.github.sinri.AiOnHttpMix.deepseek.core;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModelSeries;
import io.github.sinri.keel.core.cutter.Cutter;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DeepseekServiceMeta implements ServiceMeta {
    private final String apiKey;
    private long streamTimeout = 180_000L;

    public DeepseekServiceMeta(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        AigcMix.getVerboseLogger().info(
                "Start DeepseekServiceMeta.request",
                j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", requestBody)
        );
        return Keel.useWebClient(webClient -> {
                    return webClient.postAbs(api)
                            .bearerTokenAuthentication(apiKey)
                            .sendJsonObject(requestBody);
                })
                .compose(bufferHttpResponse -> {
                    if (bufferHttpResponse.statusCode() != 200) {
                        return Future.failedFuture(new Exception("Status code: " + bufferHttpResponse.statusCode() + "; Body: " + bufferHttpResponse.bodyAsString()));
                    }
                    return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                });
    }

    @Override
    public void requestSSE(String api, @NotNull JsonObject parameters, Promise<Void> promise, Cutter<String> cutter, String requestId) {
        AigcMix.getVerboseLogger().info(
                "Start DeepseekServiceMeta.requestSSE",
                j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", parameters)
        );
        // todo
    }

    @Override
    public SupportedModelSeries getSupportedModelSeries() {
        return SupportedModelSeries.DeepSeek;
    }

    @Override
    public long getStreamTimeout() {
        return streamTimeout;
    }

    @Override
    public ServiceMeta setStreamTimeout(long timeout) {
        this.streamTimeout = timeout;
        return this;
    }
}
