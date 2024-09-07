package io.github.sinri.AiOnHttpMix.dashscope.core;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModelSeries;
import io.github.sinri.keel.core.cutter.Cutter;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DashscopeServiceMeta implements ServiceMeta {
    private final static String hostOfDashscope = "dashscope.aliyuncs.com";

    private final static String pathOfDashscopeQwenTextGenerate = "/api/v1/services/aigc/text-generation/generation";
    private final static String endpointOfDashscopeQwenTextGenerate = "https://" + hostOfDashscope + pathOfDashscopeQwenTextGenerate;

    private final static String endpointOfDashscopeTextEmbeddingGenerate = "https://" + hostOfDashscope + "/api/v1/services/embeddings/text-embedding/text-embedding";

    private final static String pathOfDashscopeQwenMultiModalGenerate = "/api/v1/services/aigc/multimodal-generation/generation";
    private final static String endpointOfDashscopeQwenMultiModalGenerate = "https://" + hostOfDashscope + pathOfDashscopeQwenMultiModalGenerate;

    private final String apiKey;

    public DashscopeServiceMeta(String apiKey) {
        this.apiKey = apiKey;
    }

    public Future<JsonObject> callQwenTextGenerate(
            @NotNull JsonObject requestBody,
            @Nullable String requestId
    ) {
        return request(endpointOfDashscopeQwenTextGenerate, requestBody, requestId);
    }

    public Future<Void> callQwenTextGenerateStream(
            @NotNull JsonObject parameters,
            Promise<Void> promise,
            Cutter<String> cutter,
            String requestId
    ) {
        requestSSE(pathOfDashscopeQwenTextGenerate, parameters, promise, cutter, requestId);
        return promise.future();
    }

    public Future<JsonObject> callTextEmbeddingGeneration(
            @NotNull JsonObject requestBody,
            String requestId
    ) {
        return request(endpointOfDashscopeTextEmbeddingGenerate, requestBody, requestId);
    }

    public Future<JsonObject> callQwenMultiModalGenerate(
            @NotNull JsonObject requestBody,
            @Nullable String requestId
    ) {
        return request(endpointOfDashscopeQwenMultiModalGenerate, requestBody, requestId);
    }

    public Future<Void> callQwenMultiModalGenerateStream(
            @NotNull JsonObject parameters,
            Promise<Void> promise,
            Cutter<String> cutter,
            String requestId
    ) {
        requestSSE(pathOfDashscopeQwenMultiModalGenerate, parameters, promise, cutter, requestId);
        return promise.future();
    }


    @Override
    public final Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        AigcMix.getVerboseLogger().info(
                "Start DashscopeServiceMeta.request",
                j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", requestBody)
        );

        WebClient webClient = WebClient.create(Keel.getVertx());
        var req = webClient
                .postAbs(api)
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", "Bearer " + apiKey);
        return req
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    if (statusCode != 200) {
                        AigcMix.getVerboseLogger().error(
                                "Unexpected bufferHttpResponse in DashscopeServiceMeta.request",
                                j -> j
                                        .put("requestId", requestId)
                                        .put("status_code", statusCode)
                                        .put("detail", bufferHttpResponse.bodyAsString())
                        );

                        return Future.failedFuture(new AbnormalResponse(
                                statusCode, bufferHttpResponse.bodyAsString()
                        ));
                    } else {
                        JsonObject entries = bufferHttpResponse.bodyAsJsonObject();

                        AigcMix.getVerboseLogger().info(
                                "bufferHttpResponse in DashscopeServiceMeta.request",
                                j -> j
                                        .put("requestId", requestId)
                                        .put("output", entries)
                        );

                        return Future.succeededFuture(entries);
                    }
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    @Override
    public final void requestSSE(String api, @NotNull JsonObject parameters, Promise<Void> promise, Cutter<String> cutter, String requestId) {
        AigcMix.getVerboseLogger().info(
                "Start DashscopeServiceMeta.requestSSE",
                j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", parameters)
        );

        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost(hostOfDashscope)
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, api)
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + apiKey)
                            .putHeader("X-DashScope-SSE", "enable");
                    return httpClientRequest.send(parameters.toString())
                            .compose(httpClientResponse -> {
                                long timer = Keel.getVertx().setTimer(getStreamTimeout(), timeout -> {
                                    client.close();
                                    promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                    AigcMix.getVerboseLogger().warning(
                                            "Timeout in DashscopeServiceMeta.requestSSE",
                                            j -> j
                                                    .put("requestId", requestId)
                                    );
                                });
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.complete();
                                                        AigcMix.getVerboseLogger().info(
                                                                "End Success in DashscopeServiceMeta.requestSSE",
                                                                j -> j
                                                                        .put("requestId", requestId)
                                                        );
                                                    })
                                                    .onFailure(throwable -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.fail(throwable);
                                                        AigcMix.getVerboseLogger().exception(
                                                                throwable,
                                                                "End Failure in DashscopeServiceMeta.requestSSE",
                                                                j -> j
                                                                        .put("requestId", requestId)
                                                        );
                                                    });
                                        })
                                        .exceptionHandler(throwable -> {
                                            promise.fail(new RuntimeException("httpClientResponse exception", throwable));
                                            Keel.getVertx().cancelTimer(timer);
                                            AigcMix.getVerboseLogger().exception(
                                                    throwable,
                                                    "Response Failure in DashscopeServiceMeta.requestSSE",
                                                    j -> j
                                                            .put("requestId", requestId)
                                            );
                                        });
                                return Future.succeededFuture();
                            });
                })
                .onFailure(throwable -> {
                    promise.fail(new RuntimeException("HttpClient request exception for request: " + requestId, throwable));
                    AigcMix.getVerboseLogger().exception(
                            throwable,
                            "HttpClient Failure in DashscopeServiceMeta.requestSSE",
                            j -> j
                                    .put("requestId", requestId)
                    );
                });

        promise.future().andThen(ar -> {
            client.close();
        });
    }

    @Override
    public SupportedModelSeries getSupportedModelSeries() {
        return SupportedModelSeries.Qwen;
    }


}
