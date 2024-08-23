package io.github.sinri.AiOnHttpMix.dashscope.core;

import io.github.sinri.keel.core.cutter.CutterOnString;
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

public class DashscopeServiceMeta {
    private final static String hostOfDashscopeQwenTextGenerate = "dashscope.aliyuncs.com";
    private final static String pathOfDashscopeQwenTextGenerate = "/api/v1/services/aigc/text-generation/generation";
    private final static String endpointOfDashscopeQwenTextGenerate = "https://" + hostOfDashscopeQwenTextGenerate + pathOfDashscopeQwenTextGenerate;

    private final static String endpointOfDashscopeTextEmbeddingGenerate = "https://dashscope.aliyuncs.com/api/v1/services/embeddings/text-embedding/text-embedding";

    private final static String hostOfDashscopeQwenMultiModalGenerate = "dashscope.aliyuncs.com";
    private final static String pathOfDashscopeQwenMultiModalGenerate = "/api/v1/services/aigc/multimodal-generation/generation";
    private final static String endpointOfDashscopeQwenMultiModalGenerate = "https://" + hostOfDashscopeQwenMultiModalGenerate + pathOfDashscopeQwenMultiModalGenerate;

    private final String apiKey;

    public DashscopeServiceMeta(String apiKey) {
        this.apiKey = apiKey;
    }

    public Future<JsonObject> callQwenTextGenerate(
            //@Nullable String pluginHeader,
            @NotNull JsonObject requestBody,
            @Nullable String requestId
    ) {
        var req = WebClient.create(Keel.getVertx())
                .postAbs(endpointOfDashscopeQwenTextGenerate)
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", "Bearer " + apiKey);
        //        if (pluginHeader != null) {
        //            req.putHeader("X-DashScope-Plugin", pluginHeader);
        //        }
        return req
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    if (statusCode != 200) {
                        return Future.failedFuture(new Exception(
                                "Dashscope Service Response Error,"
                                        + " Status Code " + statusCode
                                        + " for request " + requestId
                                        + " Content: " + bufferHttpResponse.bodyAsString()
                        ));
                    }
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    return Future.succeededFuture(entries);
                });
    }

    public Future<Void> callQwenTextGenerateStream(
            @NotNull JsonObject parameters,
            Promise<Void> promise,
            CutterOnString cutter,
            String requestId
    ) {
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost(hostOfDashscopeQwenTextGenerate)
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, pathOfDashscopeQwenTextGenerate)
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + apiKey)
                            .putHeader("X-DashScope-SSE", "enable");
                    return httpClientRequest.send(parameters.toString())
                            .compose(httpClientResponse -> {
                                long timer = Keel.getVertx().setTimer(180_000L, timeout -> {
                                    client.close();
                                    promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                });
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.complete();
                                                    })
                                                    .onFailure(throwable -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.fail(throwable);
                                                    });
                                        })
                                        .exceptionHandler(throwable -> {
                                            promise.fail(new RuntimeException("httpClientResponse exception", throwable));
                                            Keel.getVertx().cancelTimer(timer);
                                        });
                                return Future.succeededFuture();
                            });
                })
                .onFailure(throwable -> {
                    promise.fail(new RuntimeException("HttpClient request exception for request: " + requestId, throwable));
                });

        return promise.future();
    }

    public Future<JsonObject> callTextEmbeddingGeneration(
            @NotNull JsonObject requestBody,
            String requestId
    ) {
        return WebClient.create(Keel.getVertx())
                .postAbs(endpointOfDashscopeTextEmbeddingGenerate)
                .putHeader("Content-Type", "application/json")
                .bearerTokenAuthentication(this.apiKey)
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    if (bufferHttpResponse.statusCode() != 200) {
                        return Future.failedFuture(new Exception(
                                "Dashscope Service Response Error,"
                                        + " Status Code " + bufferHttpResponse.statusCode()
                                        + " for request " + requestId
                                        + " Content: " + bufferHttpResponse.bodyAsString()
                        ));
                    }
                    return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                });
    }

    public Future<JsonObject> callQwenMultiModalGenerate(
            @NotNull JsonObject requestBody,
            @Nullable String requestId
    ) {
        var req = WebClient.create(Keel.getVertx())
                .postAbs(endpointOfDashscopeQwenMultiModalGenerate)
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", "Bearer " + apiKey);
        return req
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    if (statusCode != 200) {
                        return Future.failedFuture(new Exception(
                                "Dashscope Service Response Error,"
                                        + " Status Code " + statusCode
                                        + " for request " + requestId
                                        + " Content: " + bufferHttpResponse.bodyAsString()
                        ));
                    }
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    return Future.succeededFuture(entries);
                });
    }

    public Future<Void> callQwenMultiModalGenerateStream(
            @NotNull JsonObject parameters,
            Promise<Void> promise,
            CutterOnString cutter,
            String requestId
    ) {
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost(hostOfDashscopeQwenMultiModalGenerate)
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, pathOfDashscopeQwenMultiModalGenerate)
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + apiKey)
                            .putHeader("X-DashScope-SSE", "enable");
                    return httpClientRequest.send(parameters.toString())
                            .compose(httpClientResponse -> {
                                long timer = Keel.getVertx().setTimer(180_000L, timeout -> {
                                    client.close();
                                    promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                });
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.complete();
                                                    })
                                                    .onFailure(throwable -> {
                                                        Keel.getVertx().cancelTimer(timer);
                                                        promise.fail(throwable);
                                                    });
                                        })
                                        .exceptionHandler(throwable -> {
                                            promise.fail(new RuntimeException("httpClientResponse exception", throwable));
                                            Keel.getVertx().cancelTimer(timer);
                                        });
                                return Future.succeededFuture();
                            });
                })
                .onFailure(throwable -> {
                    promise.fail(new RuntimeException("HttpClient request exception for request: " + requestId, throwable));
                });

        return promise.future();
    }
}
