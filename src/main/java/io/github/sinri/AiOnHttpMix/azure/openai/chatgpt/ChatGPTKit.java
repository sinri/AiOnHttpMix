package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoiceDelta;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptStreamBuffer;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIResponseChunk;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.embeddings.OpenAIEmbeddingResponse;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponse;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.keel.core.cutter.Cutter;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ChatGPTKit {
    public Future<JsonObject> chat(
            AzureOpenAIServiceMeta serviceMeta,
            JsonObject parameters,
            String requestId
    ) {
        String api = "/chat/completions";
        return serviceMeta.request(api, parameters, requestId);
    }

    public Future<OpenAIChatGptResponse> chat(
            AzureOpenAIServiceMeta serviceMeta,
            OpenAIChatGptRequest parameters,
            String requestId
    ) {
        return chat(serviceMeta, parameters.toJsonObject(), requestId)
                .compose(resp -> {
                    return Future.succeededFuture(OpenAIChatGptResponse.wrap(resp));
                });
    }

    public Future<OpenAIChatGptResponse> chat(
            AzureOpenAIServiceMeta serviceMeta,
            Handler<OpenAIChatGptRequest> handler,
            String requestId
    ) {
        OpenAIChatGptRequest request = OpenAIChatGptRequest.create();
        handler.handle(request);
        return chat(serviceMeta, request, requestId);
    }

    public Future<Void> chatStream(
            AzureOpenAIServiceMeta serviceMeta,
            JsonObject parameters,
            @NotNull Handler<String> chunkHandler,
            String requestId
    ) {
        parameters.put("stream", true);
        Promise<Void> promise = Promise.promise();

        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            final String finalS = s;
            AigcMix.getVerboseLogger().info(
                    "Component Handler in ChatGPTKit.chatStream",
                    j -> j
                            .put("component", finalS)
                            .put("request_id", requestId)
            );

            s = s.replaceFirst("^data:\\s*", "");
            if (s.startsWith("[DONE]")) {
                promise.complete();
            } else {
                chunkHandler.handle(s);
            }
        });

        String api = "/chat/completions";

        serviceMeta.requestSSE(api, parameters, promise, cutter, requestId);

        return promise.future();
    }

    public Future<Void> chatStream(
            AzureOpenAIServiceMeta serviceMeta,
            Handler<OpenAIChatGptRequest> handler,
            @NotNull Handler<OpenAIResponseChunk> chunkHandler,
            String requestId
    ) {
        OpenAIChatGptRequest request = OpenAIChatGptRequest.create();
        handler.handle(request);
        return chatStream(serviceMeta, request, chunkHandler, requestId);
    }

    public Future<Void> chatStream(
            AzureOpenAIServiceMeta serviceMeta,
            OpenAIChatGptRequest parameters,
            @NotNull Handler<OpenAIResponseChunk> chunkHandler,
            String requestId
    ) {
        return this.chatStream(
                serviceMeta,
                parameters.toJsonObject(),
                s -> {
                    JsonObject entries = new JsonObject(s);
                    var responseChunk = OpenAIResponseChunk.wrap(entries);
                    chunkHandler.handle(responseChunk);
                },
                requestId
        );
    }

    public Future<AssistantMessage> chatStream(
            AzureOpenAIServiceMeta serviceMeta,
            Handler<OpenAIChatGptRequest> handler,
            String requestId
    ) {
        OpenAIChatGptRequest request = OpenAIChatGptRequest.create();
        handler.handle(request);
        return chatStream(serviceMeta, request, requestId);
    }

    public Future<AssistantMessage> chatStream(
            AzureOpenAIServiceMeta serviceMeta,
            OpenAIChatGptRequest parameters,
            String requestId
    ) {
        OpenAIChatGptStreamBuffer tempAssistantMessage = new OpenAIChatGptStreamBuffer();
        return this.chatStream(
                        serviceMeta,
                        parameters.toJsonObject(),
                        s -> {
                            try {
                                JsonObject entries = new JsonObject(s);
                                var responseChunk = OpenAIResponseChunk.wrap(entries);
                                List<OpenAIChatGptResponseChunkChoice> choices = responseChunk.getChoices();
                                if (choices.isEmpty()) return;
                                OpenAIChatGptResponseChunkChoice choiceInChunk = choices.get(0);
                                OpenAIChatGptResponseChunkChoiceDelta delta = choiceInChunk.getDelta();
                                if (delta == null) return;

                                String contentAsText = delta.getContentAsText();
                                if (contentAsText != null) {
                                    tempAssistantMessage.acceptContentFragment(contentAsText);
                                }

                                ChatGptRole role = delta.getRole();
                                if (role != null) {
                                    tempAssistantMessage.acceptRole(role);
                                }

                                List<OpenAIChatGptResponseToolCall> toolCalls = delta.getToolCalls();
                                if (toolCalls != null && !toolCalls.isEmpty()) {
                                    toolCalls.forEach(toolCall -> {
                                        String type = toolCall.getType();
                                        if (type != null) {
                                            // first time met!
                                            tempAssistantMessage.acceptToolCall(toolCall);
                                        } else {
                                            Integer index = toolCall.getIndex();
                                            OpenAIChatGptStreamBuffer.TempToolCall tempToolCall = tempAssistantMessage.getTempToolCall(index);
                                            if (tempToolCall != null) {
                                                OpenAIChatGptResponseFunctionCall function = toolCall.getFunction();
                                                if (function != null) {
                                                    OpenAIChatGptStreamBuffer.TempFunctionCall tempFunctionCall = tempToolCall.getFunction();
                                                    String arguments = function.getArguments();
                                                    if (arguments != null) {
                                                        tempFunctionCall.acceptArgumentFragment(arguments);
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Throwable e) {
                                AigcMix.getVerboseLogger().exception(e, "chunk handler exception in ChatGPTKit.chatStream", j -> j.put("request_id", requestId));
                            }
                        },
                        requestId
                )
                .compose(v -> {
                    return Future.succeededFuture(tempAssistantMessage.toAssistantMessage());
                });
    }

    public Future<OpenAIEmbeddingResponse> fetchEmbeddingTensorForText(AzureOpenAIServiceMeta serviceMeta, String input, String requestId) {
        return serviceMeta.request(
                        "/embeddings",
                        new JsonObject().put("input", input),
                        requestId
                )
                .compose(jsonObject -> {
                    return Future.succeededFuture(OpenAIEmbeddingResponse.wrap(jsonObject));
                });
    }
}
