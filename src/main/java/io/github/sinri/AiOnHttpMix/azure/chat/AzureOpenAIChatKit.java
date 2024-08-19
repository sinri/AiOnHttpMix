package io.github.sinri.AiOnHttpMix.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.AzureOpenAIKit;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/openai/reference">Azure OpenAI Service REST API reference</a>
 */
public class AzureOpenAIChatKit extends AzureOpenAIKit {
    private String requestId;

    public AzureOpenAIChatKit(String serviceName) {
        super(serviceName);
        this.requestId = UUID.randomUUID().toString();
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Request Sample
     * curl https://YOUR_RESOURCE_NAME.openai.azure.com/openai/deployments/YOUR_DEPLOYMENT_NAME/chat/completions?api-version=2023-05-15 \
     * -H "Content-Type: application/json" \
     * -H "api-key: YOUR_API_KEY" \
     * -d '{"messages":[{"role": "system", "content": "You are a helpful assistant."},{"role": "user", "content": "Does Azure OpenAI support customer managed keys?"},{"role": "assistant", "content": "Yes, customer managed keys are supported by Azure OpenAI."},{"role": "user", "content": "Do other Azure AI services support this too?"}]}'
     * Response Sample
     * {"id":"chatcmpl-6v7mkQj980V1yBec6ETrKPRqFjNw9",
     * "object":"chat.completion","created":1679072642,
     * "model":"gpt-35-turbo",
     * "usage":{"prompt_tokens":58,
     * "completion_tokens":68,
     * "total_tokens":126},
     * "choices":[{"message":{"role":"assistant",
     * "content":"Yes, other Azure AI services also support customer managed keys. Azure AI services offer multiple options for customers to manage keys, such as using Azure Key Vault, customer-managed keys in Azure Key Vault or customer-managed keys through Azure Storage service. This helps customers ensure that their data is secure and access to their services is controlled."},"finish_reason":"stop","index":0}]}
     */
    public Future<ChatResponse> callChatCompletions(ChatCompletionsParameters parameters) {
        //String url = "https://" + getResourceName() + ".openai.azure.com/openai/deployments/" + getDeployment() + "/chat/completions?api-version=" + getApiVersion();
        String api = "/chat/completions";
        return postRequest(api, parameters.toJsonObject())
                .compose(jsonObject -> {
                    return Future.succeededFuture(new ChatResponse(jsonObject));
                });
    }

    public Future<ChatResponse> callExtensionChatCompletions(ExtenstionChatCompletionsParameters parameters) {
        String api = "/extensions/chat/completions";
        return postRequest(api, parameters.toJsonObject())
                .compose(jsonObject -> {
                    return Future.succeededFuture(new ChatResponse(jsonObject));
                });
    }

    public Future<Void> callChatCompletionsStreamWithCutter(
            @NotNull ChatCompletionsParameters parameters,
            @NotNull Handler<ChatStreamResponse> parsedPieceHandler
    ) {
        parameters.setStream(true);

        Promise<Void> promise = Promise.promise();

        CutterOnString cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
//            getLogger().info(log -> log.message("debug buffer: " + buffer));
//            String s = buffer.toString(StandardCharsets.UTF_8);
//            String finalS = s;
//            getLogger().info(log -> log.message("debug buffer in utf-8: " + finalS));
            s = s.replaceFirst("^data:\\s*", "");
            if (s.startsWith("[DONE]")) {
                promise.complete();
            } else {
                var parsed = new JsonObject(s);
                ChatStreamResponse chatStreamResponse = new ChatStreamResponse(parsed);
                parsedPieceHandler.handle(chatStreamResponse);
            }

            // todo 存在案例，没有进入complete
        });

        return callAnyChatCompletionsStreamWithCutter("/chat/completions", parameters, promise, cutter);
    }


    public Future<Void> callExtensionChatCompletionsStreamWithCutter(
            @NotNull ExtenstionChatCompletionsParameters parameters,
            @NotNull Handler<ExtensionsChatCompletionChunkResponse> parsedPieceHandler
    ) {
        parameters.setStream(true);
        Promise<Void> promise = Promise.promise();
        CutterOnString cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
//            String s = buffer.toString(StandardCharsets.UTF_8);
//            String finalS = s;
//            getLogger().info(log -> log.message("debug buffer in utf-8: " + finalS));
            s = s.replaceFirst("^data:\\s*", "");

            if ("[DONE]".equals(s)) {
                promise.complete();
            } else {
                try {
                    var parsed = new JsonObject(s);
//                    getLogger().info(log -> log.message("debug buffer parsed piece").put("context", parsed));
                    ExtensionsChatCompletionChunkResponse chatStreamChunkResponse = new ExtensionsChatCompletionChunkResponse(parsed);
                    parsedPieceHandler.handle(chatStreamChunkResponse);
                } catch (Throwable e) {
                    promise.fail(e);
                }
            }
        });
        return callAnyChatCompletionsStreamWithCutter("/extensions/chat/completions", parameters, promise, cutter);
    }

    private <R extends ChatCompletionsParameters> Future<Void> callAnyChatCompletionsStreamWithCutter(
            String api,
            @NotNull R parameters,
            Promise<Void> promise,
            CutterOnString cutter
    ) {
        HttpClientOptions options = new HttpClientOptions()
                .setKeepAlive(true)
                .setSsl(true)
                .setDefaultHost(generateHost())
                .setDefaultPort(443);
        HttpClient client = Keel.getVertx().createHttpClient(options);

        client.request(HttpMethod.POST, generateUri(api))
                .andThen(ar -> {
                    if (ar.succeeded()) {
                        var httpClientRequest = ar.result();

                        httpClientRequest
                                .putHeader("Content-Type", "application/json")
                                .putHeader("api-key", getApiKey());
                        httpClientRequest.send(parameters.toJsonObject().toString())
                                .onSuccess(httpClientResponse -> {
                                    long timer = Keel.getVertx().setTimer(180_000L, timeout -> {
//                                        getLogger().error("OpenAI API SSE TIMEOUT FOR REQUEST: " + requestId);
                                        promise.tryFail("TIMEOUT FOR REQUEST " + requestId);
                                    });
                                    httpClientResponse
                                            .handler(cutter::handle)
                                            .endHandler(v -> {
//                                                getLogger().warning("CALL Azure API response END", j -> j.put("request_id", requestId));
                                                cutter.end()
                                                        .onSuccess(cutterEnded -> {
                                                            Keel.getVertx().cancelTimer(timer);
//                                                            getLogger().info("CALL Azure API response END, cutter end done", j -> j.put("request_id", requestId));
                                                            promise.complete();
                                                        })
                                                        .onFailure(throwable -> {
                                                            Keel.getVertx().cancelTimer(timer);
//                                                            getLogger().exception(throwable, "CALL Azure API response END, cutter end failed", j -> j.put("request_id", requestId));
                                                            promise.fail(throwable);
                                                        });
                                            })
                                            .exceptionHandler(throwable -> {
                                                promise.fail(new RuntimeException("httpClientResponse exception", throwable));
                                                Keel.getVertx().cancelTimer(timer);
                                            });
                                });
                    } else {
                        var throwable = ar.cause();

                        promise.fail(new RuntimeException("httpClient request exception for request: " + requestId, throwable));
                    }
                });

        return promise.future();
    }

}
