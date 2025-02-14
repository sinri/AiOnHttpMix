package io.github.sinri.AiOnHttpMix.volces.core;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.utils.SupportedProvider;
import io.github.sinri.keel.core.cutter.Cutter;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class VolcesServiceMeta implements ServiceMeta {
    public static final String pathOfV3ChatCompletions = "/api/v3/chat/completions";
    /**
     * @since 1.2.2
     */
    private static final Set<SupportedModel> supportedModels = new HashSet<>();
    private static final String hostOfV3ChatCompletions = "ark.cn-beijing.volces.com";

    static {
        // since 1.2.2
        supportedModels.add(SupportedModel.Doubao);
        supportedModels.add(SupportedModel.DeepSeekChatOnVolces);
        supportedModels.add(SupportedModel.DeepSeekReasonerOnVolces);
    }

    //private static final String endpointOfV3ChatCompletions = "https://" + hostOfV3ChatCompletions + pathOfV3ChatCompletions;
    private final @NotNull String apiKey;
    private final @NotNull String model;
    private final long streamTimeout = 180_000L;
    public VolcesServiceMeta(@NotNull String apiKey, @NotNull String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public Set<SupportedModel> getSupportedModels() {
        return supportedModels;
    }

    public @NotNull String getModel() {
        return model;
    }

    @Override
    public Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start VolcesServiceMeta.request")
                .context(j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", requestBody)
                )
        );

        WebClient webClient = WebClient.create(Keel.getVertx());
        return webClient
                .postAbs("https://" + hostOfV3ChatCompletions + api)
                .bearerTokenAuthentication(apiKey)
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    if (bufferHttpResponse.statusCode() != 200) {
                        AigcMix.getVerboseLogger().error(x -> x
                                .message("Unexpected bufferHttpResponse in VolcesServiceMeta.request")
                                .context(j -> j
                                        .put("requestId", requestId)
                                        .put("status_code", bufferHttpResponse.statusCode())
                                        .put("detail", bufferHttpResponse.bodyAsString()))
                        );
                        return Future.failedFuture(new Exception("Volces API Error: NOT 200. " + bufferHttpResponse.bodyAsString()));
                    } else {
                        JsonObject respAsJsonObject = bufferHttpResponse.bodyAsJsonObject();
                        AigcMix.getVerboseLogger().info(x -> x
                                .message("bufferHttpResponse in VolcesServiceMeta.request")
                                .context(j -> j
                                        .put("requestId", requestId)
                                        .put("output", respAsJsonObject))
                        );
                        return Future.succeededFuture(respAsJsonObject);
                    }
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    @Override
    public void requestSSE(String api, @NotNull JsonObject parameters, Promise<Void> promise, Cutter<String> cutter, int maxExecutionSeconds, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start VolcesServiceMeta.requestSSE")
                .context(j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", parameters))
        );

        Keel.useHttpClient(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(hostOfV3ChatCompletions)
                        .setDefaultPort(443),
                client -> {
                    return client.request(HttpMethod.POST, api)
                                 .compose(httpClientRequest -> {
                                     httpClientRequest
                                             .putHeader("Content-Type", "application/json")
                                             .putHeader("Authorization", "Bearer " + apiKey);
                                     return httpClientRequest
                                             .send(parameters.toString())
                                             .onSuccess(httpClientResponse -> {
                                                 Long timer;
                                                 if (maxExecutionSeconds > 0) {
                                                     timer = Keel.getVertx()
                                                                 .setTimer(maxExecutionSeconds * 1000L, timeout -> {
                                                                     promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                                                     AigcMix.getVerboseLogger()
                                                                            .info(x -> x
                                                                                    .message("Timeout in VolcesServiceMeta.requestSSE")
                                                                                    .context(j -> j
                                                                                            .put("requestId", requestId))
                                                                            );
                                                                 });
                                                 } else {
                                                     timer = null;
                                                 }
                                                 httpClientResponse
                                                         .handler(cutter::handle)
                                                         .endHandler(v -> {
                                                             // seems volces SSE does not auto end
                                                             cutter.end()
                                                                   .onSuccess(cutterEnded -> {
                                                                       promise.complete();
                                                                       AigcMix.getVerboseLogger()
                                                                              .info(x -> x
                                                                                      .message("End Success in VolcesServiceMeta.requestSSE")
                                                                                      .context(j -> j
                                                                                              .put("requestId", requestId))
                                                                              );
                                                                   })
                                                                   .onFailure(throwable -> {
                                                                       promise.fail(throwable);
                                                                       AigcMix.getVerboseLogger()
                                                                              .exception(
                                                                                      throwable,
                                                                                      x -> x.message("End Failure in VolcesServiceMeta.requestSSE")
                                                                                            .context(j -> j
                                                                                                    .put("requestId", requestId))
                                                                              );
                                                                   });
                                                         })
                                                         .exceptionHandler(throwable -> {
                                                             promise.fail(new RuntimeException("httpClientResponse exception", throwable));
                                                             AigcMix.getVerboseLogger().exception(
                                                                     throwable,
                                                                     x -> x.message("HttpClient Response Failure in VolcesServiceMeta.requestSSE")
                                                                           .context(j -> j
                                                                                   .put("requestId", requestId))
                                                             );
                                                         });
                                             });
                                 })
                                 .onFailure(throwable -> {
                                     promise.fail(new RuntimeException("httpClient request exception", throwable));
                                     AigcMix.getVerboseLogger().exception(
                                             throwable,
                                             x -> x.message("HttpClient Failure in VolcesServiceMeta.requestSSE")
                                                   .context(j -> j
                                                           .put("requestId", requestId))
                                     );
                                 })
                                 .eventually(promise::future);
                }
        );
    }

    @Override
    public SupportedProvider getSupportedProvider() {
        return SupportedProvider.Volces;
    }


}
