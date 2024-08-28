package io.github.sinri.AiOnHttpMix.volces.core;

import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class VolcesServiceMeta implements ServiceMeta {
    private static final String hostOfV3ChatCompletions = "ark.cn-beijing.volces.com";
    public static final String pathOfV3ChatCompletions = "/api/v3/chat/completions";
    //private static final String endpointOfV3ChatCompletions = "https://" + hostOfV3ChatCompletions + pathOfV3ChatCompletions;
    private final @NotNull String apiKey;
    private final @NotNull String model;

    public @NotNull String getModel() {
        return model;
    }

    public VolcesServiceMeta(@NotNull String apiKey, @NotNull String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public Future<JsonObject> request(String api, JsonObject requestBody, String requestId) {
        WebClient webClient = WebClient.create(Keel.getVertx());
        return webClient
                .postAbs("https://" + hostOfV3ChatCompletions + api)
                .bearerTokenAuthentication(apiKey)
                .sendJsonObject(requestBody)
                .compose(bufferHttpResponse -> {
                    if (bufferHttpResponse.statusCode() != 200) {
                        return Future.failedFuture(new Exception("Volces API Error: NOT 200. " + bufferHttpResponse.bodyAsString()));
                    }
                    JsonObject respAsJsonObject = bufferHttpResponse.bodyAsJsonObject();
                    return Future.succeededFuture(respAsJsonObject);
                    //ChatCompletionsResponse chatCompletionsResponse = new ChatCompletionsResponse(respAsJsonObject);
                    //return Future.succeededFuture(chatCompletionsResponse);
                })
                .andThen(ar -> {
                    webClient.close();
                });
    }

    @Override
    public void requestSSE(String api, @NotNull JsonObject parameters, Promise<Void> promise, CutterOnString cutter, String requestId) {
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost(hostOfV3ChatCompletions)
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, api)
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + apiKey);
                    return httpClientRequest.send(parameters.toString())
                            .onSuccess(httpClientResponse -> {
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            // Keel.getLogger().fatal("CALL Volces API response END");
                                            cutter.end()
                                                    .onSuccess(cutterEnded -> {
                                                        promise.complete();
                                                    })
                                                    .onFailure(throwable -> {
                                                        promise.fail(throwable);
                                                    });
                                        })
                                        .exceptionHandler(throwable -> {
                                            promise.fail(new RuntimeException("httpClientResponse exception", throwable));
                                        });
                            });
                })
                .onFailure(throwable -> {
                    promise.fail(new RuntimeException("httpClient request exception", throwable));
                });

        promise.future().andThen(ar -> {
            client.close();
        });
    }
}
