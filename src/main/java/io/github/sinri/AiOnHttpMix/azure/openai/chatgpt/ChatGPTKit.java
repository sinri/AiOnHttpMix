package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.chunk.ResponseChunkImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.chunk.TempAssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.embeddings.EmbeddingResponseImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.request.ChatCompletionsParametersImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.request.ToolDefinitionImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.response.CreateChatCompletionResponseImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.*;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ChatGPTKit {
    private static final ChatGPTKit instance = new ChatGPTKit();

    private ChatGPTKit() {
    }

    public static ChatGPTKit getInstance() {
        return instance;
    }

    public Future<JsonObject> callChatCompletions(
            AzureOpenAIServiceMeta serviceMeta,
            JsonObject parameters,
            String requestId
    ) {
        String api = "/chat/completions";
        return serviceMeta.postRequest(api, parameters, requestId);
    }

    public Future<CreateChatCompletionResponse> callChatCompletions(
            AzureOpenAIServiceMeta serviceMeta,
            ChatCompletionsParameters parameters,
            String requestId
    ) {
        return callChatCompletions(serviceMeta, parameters.toJsonObject(), requestId)
                .compose(resp -> {
                    return Future.succeededFuture(CreateChatCompletionResponse.wrap(resp));
                });
    }

    public Future<Void> callChatCompletionsStream(
            AzureOpenAIServiceMeta serviceMeta,
            JsonObject parameters,
            @NotNull Handler<String> chunkHandler,
            String requestId
    ) {
        parameters.put("stream", true);
        Promise<Void> promise = Promise.promise();

        CutterOnString cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            s = s.replaceFirst("^data:\\s*", "");
            if (s.startsWith("[DONE]")) {
                promise.complete();
            } else {
                //var parsed = new JsonObject(s);
                chunkHandler.handle(s);
            }
        });

        String api = "/chat/completions";

        serviceMeta.postRequestSSE(api, parameters, promise, cutter, requestId);

        return promise.future();
    }

    public Future<Void> callChatCompletionsStream(
            AzureOpenAIServiceMeta serviceMeta,
            ChatCompletionsParameters parameters,
            @NotNull Handler<ResponseChunk> chunkHandler,
            String requestId
    ) {
        return this.callChatCompletionsStream(
                serviceMeta,
                parameters.toJsonObject(),
                s -> {
                    JsonObject entries = new JsonObject(s);
                    ResponseChunkImpl responseChunk = new ResponseChunkImpl(entries);
                    chunkHandler.handle(responseChunk);
                },
                requestId
        );
    }

    public Future<AssistantMessage> callChatCompletionsStream(
            AzureOpenAIServiceMeta serviceMeta,
            ChatCompletionsParameters parameters,
            String requestId
    ) {
        TempAssistantMessage tempAssistantMessage = new TempAssistantMessage();
        return this.callChatCompletionsStream(
                        serviceMeta,
                        parameters.toJsonObject(),
                        s -> {
                            try {
                                JsonObject entries = new JsonObject(s);
                                ResponseChunkImpl responseChunk = new ResponseChunkImpl(entries);
                                List<OpenAIResponseChunkMixin.ChoiceInChunk> choices = responseChunk.getChoices();
                                if (choices.isEmpty()) return;
                                OpenAIResponseChunkMixin.ChoiceInChunk choiceInChunk = choices.get(0);
                                OpenAIResponseChunkMixin.Delta delta = choiceInChunk.getDelta();
                                if (delta == null) return;

                                String contentAsText = delta.getContentAsText();
                                if (contentAsText != null) {
                                    tempAssistantMessage.acceptContentFragment(contentAsText);
                                }

                                OpenAIMessageMixin.ChatCompletionRequestMessageRole role = delta.getRole();
                                if (role != null) {
                                    tempAssistantMessage.acceptRole(role);
                                }

                                List<ToolCallInChunk> toolCalls = delta.getToolCalls();
                                if (toolCalls != null && !toolCalls.isEmpty()) {
                                    toolCalls.forEach(toolCall -> {
                                        String type = toolCall.getType();
                                        if (type != null) {
                                            // first time met!
                                            tempAssistantMessage.acceptToolCall(toolCall);
                                        } else {
                                            Integer index = toolCall.getIndex();
                                            TempAssistantMessage.TempToolCall tempToolCall = tempAssistantMessage.getTempToolCall(index);
                                            if (tempToolCall != null) {
                                                OpenAIToolCallMixin.FunctionCall function = toolCall.getFunction();
                                                if (function != null) {
                                                    TempAssistantMessage.TempFunctionCall tempFunctionCall = tempToolCall.getFunction();
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
                                e.printStackTrace();
                            }
                        },
                        requestId
                )
                .compose(v -> {
                    return Future.succeededFuture(tempAssistantMessage.toAssistantMessage());
                });
    }

    public Future<EmbeddingResponse> callEmbeddings(AzureOpenAIServiceMeta serviceMeta, String input, String requestId) {
        return serviceMeta.postRequest(
                        "/embeddings",
                        new JsonObject().put("input", input),
                        requestId
                )
                .compose(jsonObject -> {
                    return Future.succeededFuture(EmbeddingResponse.wrap(jsonObject));
                });
    }

    public interface EmbeddingResponse extends OpenAIEmbeddingResponseMixin {
        static EmbeddingResponse wrap(JsonObject jsonObject) {
            return new EmbeddingResponseImpl(jsonObject);
        }
    }

    /**
     * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#chat-completions">Chat completions</a>
     */
    public interface ChatCompletionsParameters extends OpenAIChatCompletionsParametersMixin<ChatCompletionsParameters> {
        static ChatCompletionsParameters create() {
            return new ChatCompletionsParametersImpl();
        }
    }

    public interface Message extends OpenAIMessageMixin<Message> {
        static Builder builder() {
            return new Builder();
        }
    }

    public interface ToolDefinition extends OpenAIToolDefinitionMixin<ToolDefinition> {

        static Builder builder() {
            return new ToolDefinitionImpl.FunctionToolDefinitionBuilder();
        }
    }

    public interface ToolCall extends OpenAIToolCallMixin {
    }

    public interface ToolCallInChunk extends ToolCall {
    }

    public interface CreateChatCompletionResponse extends OpenAICreateChatCompletionResponseMixin {
        static CreateChatCompletionResponse wrap(JsonObject response) {
            return new CreateChatCompletionResponseImpl(response);
        }
    }

    public interface ResponseChunk extends OpenAIResponseChunkMixin {
    }
}
