package io.github.sinri.AiOnHttpMix.moonshot;


import io.github.sinri.AiOnHttpMix.moonshot.chat.ChatCompletionsRequest;
import io.github.sinri.AiOnHttpMix.moonshot.chat.ChatCompletionsResponse;
import io.github.sinri.AiOnHttpMix.moonshot.chat.ChatCompletionsStreamResponse;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.github.sinri.keel.logger.event.KeelEventLogger;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MoonshotKimiClient {
    public static final String endpoint = "https://api.moonshot.cn/v1";
    private final String apiKey;

//    public MoonshotKimiClient() {
//        this(Objects.requireNonNull(Keel.config("moonshot.apiKey")));
//    }

    public MoonshotKimiClient(@NotNull String apiKey) {
        this.apiKey = apiKey;
    }


    public Future<JsonObject> getModels() {
        return WebClient.create(Keel.getVertx())
                .getAbs(endpoint + "/models")
                .bearerTokenAuthentication(apiKey)
                .send()
                .compose(bufferHttpResponse -> {
                    return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                });
    }

    private Future<JsonObject> call(@NotNull String api, @NotNull JsonObject body) {
        return Future.succeededFuture(endpoint + api)
                .compose(url -> {
                    var req = WebClient.create(Keel.getVertx())
                            .postAbs(url)
                            .bearerTokenAuthentication(apiKey);
                    return Future.succeededFuture(req);
                })
                .compose(req -> {
                    return req.sendJsonObject(body);
                })
                .compose(bufferHttpResponse -> {
                    //this.logger.debug("resp: " + bufferHttpResponse.statusCode() + " \n " + bufferHttpResponse.bodyAsString());
                    if (bufferHttpResponse.statusCode() == 200) {
                        return Future.succeededFuture(bufferHttpResponse.bodyAsJsonObject());
                    } else {
                        return Future.failedFuture("not 200");
                    }
                });
    }

    public Future<ChatCompletionsResponse> callChatCompletionsWithoutSSE(@NotNull ChatCompletionsRequest chatCompletionsRequest) {
        return this.call("/chat/completions", chatCompletionsRequest.toJsonObject())
                .compose(resp -> {
                    //this.logger.info("resp", resp);
                    return Future.succeededFuture(new ChatCompletionsResponse(resp));
                });
    }

    public Future<Void> callChatCompletionsWithSSE(
            @NotNull ChatCompletionsRequest chatCompletionsRequest,
            @NotNull Handler<ChatCompletionsStreamResponse> parsedPieceHandler
    ) {
        chatCompletionsRequest.setStream(true);
        Promise<Void> promise = Promise.promise();

        CutterOnString cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            s = s.replaceFirst("^data:\\s*", "");
            if (s.startsWith("[DONE]")) {
                promise.complete();
            } else {
                //getLogger().warning("piece: " + s);
                var parsed = new JsonObject(s);
                ChatCompletionsStreamResponse chatStreamResponse = new ChatCompletionsStreamResponse(parsed);
                parsedPieceHandler.handle(chatStreamResponse);
            }
        });

        return callAnyChatCompletionsStreamWithCutter(chatCompletionsRequest, promise, cutter);
    }

    private Future<Void> callAnyChatCompletionsStreamWithCutter(
            @NotNull ChatCompletionsRequest parameters,
            Promise<Void> promise,
            CutterOnString cutter
    ) {
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost("api.moonshot.cn")
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, "/v1/chat/completions")
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + this.apiKey);
                    return httpClientRequest.send(parameters.toJsonObject().toString())
                            .onSuccess(httpClientResponse -> {
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            //getLogger().warning("CALL Moonshot API response END");
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
