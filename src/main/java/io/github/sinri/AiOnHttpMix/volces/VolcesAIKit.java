package io.github.sinri.AiOnHttpMix.volces;


import io.github.sinri.AiOnHttpMix.volces.chat.ChatCompletionsRequest;
import io.github.sinri.AiOnHttpMix.volces.chat.ChatCompletionsResponse;
import io.github.sinri.AiOnHttpMix.volces.chat.ChatCompletionsSSEResponse;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;


import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class VolcesAIKit {

    public VolcesAIKit() {

    }

    public Future<ChatCompletionsResponse> callChatCompletionsV3ApiWithoutSSE(
            //@NotNull String deployName,
            @NotNull String apiKey,
            @NotNull String model,
            @NotNull ChatCompletionsRequest chatCompletionsRequest
    ) {
//        String apiKey = Keel.config("volces." + deployName + ".apiKey");
//        String model = Keel.config("volces." + deployName + ".model");
        JsonObject body = chatCompletionsRequest
                .setModel(model)
                .setStream(false)
                .toJsonObject();
        return WebClient.create(Keel.getVertx())
                .postAbs("https://ark.cn-beijing.volces.com/api/v3/chat/completions")
                .bearerTokenAuthentication(apiKey)
                .sendJsonObject(body)
                .compose(bufferHttpResponse -> {
                    if (bufferHttpResponse.statusCode() != 200) {
                        return Future.failedFuture(new Exception("Volces API Error: NOT 200. " + bufferHttpResponse.bodyAsString()));
                    }
                    JsonObject respAsJsonObject = bufferHttpResponse.bodyAsJsonObject();
                    ChatCompletionsResponse chatCompletionsResponse = new ChatCompletionsResponse(respAsJsonObject);
                    return Future.succeededFuture(chatCompletionsResponse);
                });
    }

    public Future<Void> callChatCompletionsV3ApiWithSSE(
//            @NotNull String deployName,
            @NotNull String apiKey,
            @NotNull String model,
            @NotNull ChatCompletionsRequest chatCompletionsRequest,
            Handler<ChatCompletionsSSEResponse> parsedPieceHandler
    ) {
        Promise<Void> promise = Promise.promise();

        CutterOnString cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            s = s.replaceFirst("^data:\\s*", "");
            if (s.startsWith("[DONE]")) {
                promise.complete();
            } else {
                // Keel.getLogger().fatal("piece: " + s);
                var parsed = new JsonObject(s);
                ChatCompletionsSSEResponse chatStreamResponse = new ChatCompletionsSSEResponse(parsed);
                parsedPieceHandler.handle(chatStreamResponse);
            }
        });

//        String apiKey = Keel.config("volces." + deployName + ".apiKey");
//        String model = Keel.config("volces." + deployName + ".model");
        JsonObject body = chatCompletionsRequest
                .setModel(model)
                .setStream(true)
                .toJsonObject();

        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost("ark.cn-beijing.volces.com")
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, "/api/v3/chat/completions")
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + apiKey);
                    return httpClientRequest.send(body.toString())
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

        return promise.future();
    }
}
