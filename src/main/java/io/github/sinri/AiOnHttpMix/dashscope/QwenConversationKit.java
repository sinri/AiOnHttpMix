package io.github.sinri.AiOnHttpMix.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.conversation.Message;
import io.github.sinri.AiOnHttpMix.dashscope.conversation.Parameters;
import io.github.sinri.AiOnHttpMix.dashscope.conversation.Response;
import io.github.sinri.AiOnHttpMix.dashscope.conversation.SSEResponseChunk;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.github.sinri.keel.logger.event.KeelEventLogger;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class QwenConversationKit {
    //qwen-turbo、qwen-plus、qwen-max、qwen-max-1201和qwen-max-longcontext
    public static final String Model_OF_QWEN_TRUBO = "qwen-turbo";
    public static final String Model_OF_QWEN_PLUS = "qwen-plus";
    public static final String Model_OF_QWEN_MAX = "qwen-max";
    public static final String Model_OF_QWEN_MAX_1201 = "qwen-max-1201";
    public static final String Model_OF_QWEN_MAX_LONG_CONTEXT = "qwen-max-longcontext";


    private final String apiKey;

    public QwenConversationKit(String apiKey) {
        this.apiKey = apiKey;
    }

//    public QwenConversationKit() {
//        this(Keel.config("dashscope.api_key"));
//    }

    public Future<Void> callWithSSE(
            @NotNull String model,
            @NotNull List<Message> messages,
            @NotNull Parameters parameters,
            Handler<SSEResponseChunk> chunkHandler,
            @Nullable String pluginHeader
    ) {
        // Note: 2024Apr16,Great Jie Brother agreed to remove this limitation.
        // parameters.setIncrementalOutput(true);
        var body = prepareBody(model, messages, parameters);
        Promise<Void> promise = Promise.promise();
        CutterOnString cutterOnString = new CutterOnString();

        cutterOnString.setComponentHandler(s -> {
            var chunk = new SSEResponseChunk(s);
            chunkHandler.handle(chunk);
        });

        return callWithSSEImpl(body, promise, cutterOnString, pluginHeader);
    }

    private Future<Void> callWithSSEImpl(
            JsonObject body,
            Promise<Void> promise,
            CutterOnString cutter,
            @Nullable String pluginHeader
    ) {
        // 服务器发送事件 (Server-sent events)
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost("dashscope.aliyuncs.com")
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, "/api/v1/services/aigc/text-generation/generation")
                .compose(httpClientRequest -> {
                    httpClientRequest
                            .putHeader("Content-Type", "application/json")
                            .putHeader("Authorization", "Bearer " + apiKey)
                            .putHeader("X-DashScope-SSE", "enable");
                    if (pluginHeader != null) {
                        httpClientRequest.putHeader("X-DashScope-Plugin", pluginHeader);
                    }

                    return httpClientRequest.send(body.toString())
                            .onSuccess(httpClientResponse -> {
                                httpClientResponse
                                        .handler(cutter::handle)
                                        .endHandler(v -> {
                                            //logger.warning("CALL Qwen API response END");
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

    public Future<Response> callWithoutSSE(
            @NotNull String model,
            @NotNull List<Message> messages,
            @NotNull Parameters parameters,
            @Nullable String pluginHeader
    ) {
        var body = prepareBody(model, messages, parameters);
        var req = WebClient.create(Keel.getVertx())
                .postAbs("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation")
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", "Bearer " + apiKey);
        if (pluginHeader != null) {
            req.putHeader("X-DashScope-Plugin", pluginHeader);
        }
        return req
                .sendJsonObject(body)
                .compose(bufferHttpResponse -> {
                    int statusCode = bufferHttpResponse.statusCode();
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
//                    this.logger.info(eventLog -> eventLog
//                            .message("api responded " + statusCode)
//                            .context(c -> c
//                                    .put("input", body)
//                                    .put("output", entries)
//                            )
//                    );
                    return Future.succeededFuture(new Response(
                            statusCode,
                            entries
                    ));
                });
    }

    private JsonObject prepareBody(
            @NotNull String model,
            @NotNull List<Message> messages,
            @NotNull Parameters parameters
    ) {
        JsonObject body = new JsonObject();
        body.put("model", model);

        JsonObject input = new JsonObject();
        JsonArray messagesArray = new JsonArray();
        messages.forEach(message -> {
            messagesArray.add(message.toJsonObject());
        });
        input.put("messages", messagesArray);
        body.put("input", input);

        body.put("parameters", parameters.toJsonObject());

        return body;
    }

}
