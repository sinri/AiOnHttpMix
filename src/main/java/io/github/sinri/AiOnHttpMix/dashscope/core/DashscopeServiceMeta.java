package io.github.sinri.AiOnHttpMix.dashscope.core;

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
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DashscopeServiceMeta implements ServiceMeta {
    /**
     * @since 1.2.2
     */
    private static final Set<SupportedModel> supportedModels = new HashSet<>();
    private final static String hostOfDashscope = "dashscope.aliyuncs.com";
    private final static String pathOfDashscopeQwenTextGenerate = "/api/v1/services/aigc/text-generation/generation";
    private final static String endpointOfDashscopeQwenTextGenerate = "https://" + hostOfDashscope + pathOfDashscopeQwenTextGenerate;
    private final static String endpointOfDashscopeTextEmbeddingGenerate = "https://" + hostOfDashscope + "/api/v1/services/embeddings/text-embedding/text-embedding";
    private final static String pathOfDashscopeQwenMultiModalGenerate = "/api/v1/services/aigc/multimodal-generation/generation";
    private final static String endpointOfDashscopeQwenMultiModalGenerate = "https://" + hostOfDashscope + pathOfDashscopeQwenMultiModalGenerate;
    private final static String endpointOfDashscopeWanxiangImageSynthesis = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text2image/image-synthesis";
    private final static String endpointOfDashscopeAsyncTaskQuery = "https://dashscope.aliyuncs.com/api/v1/tasks/";//{task_id}

    static {
        // since 1.2.2
        supportedModels.add(SupportedModel.QwenMax);
        supportedModels.add(SupportedModel.QwenPlus);
        supportedModels.add(SupportedModel.QwenLong);
        supportedModels.add(SupportedModel.DeepSeekChatOnDashScope);
        supportedModels.add(SupportedModel.DeepSeekReasonerOnDashScope);
    }

    private final String apiKey;
    private final long streamTimeout = 180_000L;
    public DashscopeServiceMeta(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public Set<SupportedModel> getSupportedModels() {
        return supportedModels;
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
            int maxExecutionSeconds,
            String requestId
    ) {
        requestSSE(pathOfDashscopeQwenTextGenerate, parameters, promise, cutter, maxExecutionSeconds, requestId);
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
            int maxExecutionSeconds,
            String requestId
    ) {
        requestSSE(pathOfDashscopeQwenMultiModalGenerate, parameters, promise, cutter, maxExecutionSeconds, requestId);
        return promise.future();
    }

    /**
     * @since 1.1.6
     */
    public Future<JsonObject> callWanxiangImageSynthesis(JsonObject requestBody, String requestId) {
        return request(
                endpointOfDashscopeWanxiangImageSynthesis,
                Map.of("X-DashScope-Async", "enable"),
                requestBody,
                requestId
        );
    }

    public Future<JsonObject> callAsyncTaskQuery(String taskId, String requestId) {
        return Keel.useWebClient(webClient -> {
                       return webClient.getAbs(endpointOfDashscopeAsyncTaskQuery + taskId)
                                       .putHeader("Authorization", "Bearer " + apiKey)
                                       .send();
                   })
                   .compose(resp -> {
                       var r = resp.bodyAsJsonObject();
                       return Future.succeededFuture(r);
                   });
    }

    @Override
    public final Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        return this.request(api, Map.of(), requestBody, requestId);
    }

    /**
     * @since 1.1.6
     */
    public final Future<JsonObject> request(String api, Map<String, String> headers, JsonObject requestBody, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start DashscopeServiceMeta.request")
                .context(j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", requestBody)
                )
        );

        WebClient webClient = WebClient.create(Keel.getVertx());
        var req = webClient
                .postAbs(api)
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", "Bearer " + apiKey);

        headers.forEach(req::putHeader);

        return req
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    if (statusCode != 200) {
                        AigcMix.getVerboseLogger().error(x -> x
                                .message("Unexpected bufferHttpResponse in DashscopeServiceMeta.request")
                                .context(
                                        j -> j
                                                .put("requestId", requestId)
                                                .put("status_code", statusCode)
                                                .put("detail", bufferHttpResponse.bodyAsString())
                                )
                        );

                        return Future.failedFuture(new AbnormalResponse(
                                statusCode, bufferHttpResponse.bodyAsString()
                        ));
                    } else {
                        JsonObject entries = bufferHttpResponse.bodyAsJsonObject();

                        AigcMix.getVerboseLogger().info(x -> x
                                .message("bufferHttpResponse in DashscopeServiceMeta.request")
                                .context(j -> j
                                        .put("requestId", requestId)
                                        .put("output", entries))
                        );

                        return Future.succeededFuture(entries);
                    }
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    @Override
    public final void requestSSE(String api, @NotNull JsonObject parameters, Promise<Void> promise, Cutter<String> cutter, int maxExecutionSeconds, String requestId) {
        AigcMix.getVerboseLogger().info(x -> x
                .message("Start DashscopeServiceMeta.requestSSE")
                .context(j -> j
                        .put("api", api)
                        .put("requestId", requestId)
                        .put("input", parameters))
        );

        Keel.useHttpClient(
                new HttpClientOptions()
                        .setKeepAlive(true)
                        .setSsl(true)
                        .setDefaultHost(hostOfDashscope)
                        .setDefaultPort(443),
                client -> {
                    return client.request(HttpMethod.POST, api)
                                 .compose(httpClientRequest -> {
                                     httpClientRequest
                                             .putHeader("Content-Type", "application/json")
                                             .putHeader("Authorization", "Bearer " + apiKey)
                                             .putHeader("X-DashScope-SSE", "enable");
                                     return httpClientRequest
                                             .send(parameters.toString())
                                             .compose(httpClientResponse -> {
                                                 Long timer;
                                                 if (maxExecutionSeconds > 0) {
                                                     timer = Keel.getVertx()
                                                                 .setTimer(maxExecutionSeconds * 1000L, timeout -> {
                                                                     client.close();
                                                                     promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                                                     AigcMix.getVerboseLogger().warning(x -> x
                                                                             .message("Timeout in DashscopeServiceMeta.requestSSE")
                                                                             .context(j -> j
                                                                                     .put("requestId", requestId)
                                                                             )
                                                                     );
                                                                 });
                                                 } else {
                                                     timer = null;
                                                 }
                                                 httpClientResponse
                                                         .handler(cutter::handle)
                                                         .endHandler(v -> {
                                                             cutter.end()
                                                                   .onSuccess(cutterEnded -> {
                                                                       if (timer != null) {
                                                                           Keel.getVertx().cancelTimer(timer);
                                                                       }
                                                                       promise.complete();
                                                                       AigcMix.getVerboseLogger().info(x -> x
                                                                               .message("End Success in DashscopeServiceMeta.requestSSE")
                                                                               .context(j -> j
                                                                                       .put("requestId", requestId))
                                                                       );
                                                                   })
                                                                   .onFailure(throwable -> {
                                                                       if (timer != null) {
                                                                           Keel.getVertx().cancelTimer(timer);
                                                                       }
                                                                       promise.fail(throwable);
                                                                       AigcMix.getVerboseLogger().exception(
                                                                               throwable,
                                                                               x -> x.message("End Failure in DashscopeServiceMeta.requestSSE")
                                                                                     .context(j -> j
                                                                                             .put("requestId", requestId))
                                                                       );
                                                                   });
                                                         })
                                                         .exceptionHandler(throwable -> {
                                                             promise.fail(new RuntimeException("httpClientResponse exception", throwable));
                                                             if (timer != null) {
                                                                 Keel.getVertx().cancelTimer(timer);
                                                             }
                                                             AigcMix.getVerboseLogger().exception(
                                                                     throwable,
                                                                     x -> x.message("Response Failure in DashscopeServiceMeta.requestSSE")
                                                                           .context(j -> j
                                                                                   .put("requestId", requestId))
                                                             );
                                                         });
                                                 return Future.succeededFuture();
                                             });
                                 })
                                 .onFailure(throwable -> {
                                     promise.fail(new RuntimeException("HttpClient request exception for request: " + requestId, throwable));
                                     AigcMix.getVerboseLogger().exception(
                                             throwable,
                                             x -> x.message("HttpClient Failure in DashscopeServiceMeta.requestSSE")
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
        return SupportedProvider.DashScope;
    }

}
