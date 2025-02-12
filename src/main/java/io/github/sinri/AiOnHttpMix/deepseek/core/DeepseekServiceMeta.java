package io.github.sinri.AiOnHttpMix.deepseek.core;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedProvider;
import io.github.sinri.keel.core.cutter.Cutter;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DeepseekServiceMeta implements ServiceMeta {
    public static final String ENDPOINT_SCHEMA = "https";
    public static final String ENDPOINT_HOST = "api.deepseek.com";
    public static final int ENDPOINT_PORT = 443;

    private final String apiKey;
    private final long streamTimeout = 180_000L;

    public DeepseekServiceMeta(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start DeepseekServiceMeta.request")
                .context(j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", requestBody))
        );
        return Keel.useWebClient(webClient -> {
                       return webClient.postAbs(ENDPOINT_SCHEMA + "://" + ENDPOINT_HOST + api)
                                       .bearerTokenAuthentication(apiKey)
                                       .sendJsonObject(requestBody);
                   })
                   .compose(bufferHttpResponse -> {
                       AigcMix.getVerboseLogger()
                              .debug("bufferHttpResponse code is " + bufferHttpResponse.statusCode());
                       AigcMix.getVerboseLogger()
                              .debug("bufferHttpResponse body is " + bufferHttpResponse.bodyAsString());
                       if (bufferHttpResponse.statusCode() != 200) {
                           return Future.failedFuture(new Exception("Status code: " + bufferHttpResponse.statusCode() + "; Body: " + bufferHttpResponse.bodyAsString()));
                       }
                       return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                   });
    }

    @Override
    public void requestSSE(String api, @NotNull JsonObject parameters, Promise<Void> promise, Cutter<String> cutter, int maxExecutionSeconds, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start DeepseekServiceMeta.requestSSE")
                .context(j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", parameters)
                )
        );
        Keel.useHttpClient(
                new HttpClientOptions()
                        .setSsl(true)
                        .setKeepAlive(true),
                httpClient -> httpClient.request(HttpMethod.POST, ENDPOINT_PORT, ENDPOINT_HOST, api)
                                        .compose(request -> request
                                                .putHeader("Content-Type", "application/json")
                                                .putHeader("Authorization", "Bearer " + apiKey)
                                                .send(parameters.toBuffer())
                                                .compose(response -> {
                                                    Long timer;
                                                    if (maxExecutionSeconds > 0) {
                                                        timer = Keel.getVertx()
                                                                    .setTimer(maxExecutionSeconds * 1000L, timeout -> {
                                                                        promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                                                        AigcMix.getVerboseLogger().info(x -> x
                                                                                .message("Timeout in DeepseekServiceMeta.requestSSE")
                                                                                .context(j -> j
                                                                                        .put("requestId", requestId))
                                                                        );
                                                                    });
                                                    } else {
                                                        timer = null;
                                                    }
                                                    response
                                                            .handler(buffer -> {
                                                                AigcMix.getVerboseLogger().info(x -> {
                                                                    x.message("buffer: " + buffer);
                                                                });
                                                                cutter.handle(buffer);
                                                            })
                                                            .endHandler(ended -> {
                                                                if (timer != null) {
                                                                    Keel.getVertx().cancelTimer(timer);
                                                                }
                                                                cutter.end()
                                                                      .onComplete(ar -> {
                                                                          promise.complete();
                                                                      });
                                                            })
                                                            .exceptionHandler(throwable -> {
                                                                if (timer != null) {
                                                                    Keel.getVertx().cancelTimer(timer);
                                                                }
                                                                cutter.end()
                                                                      .onComplete(ar -> {
                                                                          promise.fail(throwable);
                                                                      });
                                                            });
                                                    return Future.succeededFuture();
                                                })
                                        )
                                        .onFailure(throwable -> {
                                            cutter.end();
                                            promise.fail(throwable);
                                        })
                                        .eventually(promise::future)
        );
    }

    @Override
    public SupportedProvider getSupportedProvider() {
        return SupportedProvider.DeepSeek;
    }
}
