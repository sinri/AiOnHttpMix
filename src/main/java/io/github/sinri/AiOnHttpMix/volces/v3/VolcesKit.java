package io.github.sinri.AiOnHttpMix.volces.v3;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.v3.impl.*;
import io.github.sinri.AiOnHttpMix.volces.v3.mixin.chunk.VolcesChatCompletionsResponseChunkMixin;
import io.github.sinri.AiOnHttpMix.volces.v3.mixin.request.*;
import io.github.sinri.AiOnHttpMix.volces.v3.mixin.response.*;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public class VolcesKit {
    public Future<JsonObject> chat(VolcesServiceMeta serviceMeta, JsonObject requestBody, String requestId) {
        requestBody.put("model", serviceMeta.getModel());
        return serviceMeta.request(
                VolcesServiceMeta.pathOfV3ChatCompletions,
                requestBody,
                requestId
        );
    }

    public Future<ChatCompletionsResponse> chat(VolcesServiceMeta serviceMeta, ChatCompletionsRequest requestBody, String requestId) {
        requestBody.setModel(serviceMeta.getModel());
        return serviceMeta.request(
                        VolcesServiceMeta.pathOfV3ChatCompletions,
                        requestBody.toJsonObject(),
                        requestId
                )
                .compose(resp -> {
                    return Future.succeededFuture(ChatCompletionsResponse.wrap(resp));
                });
    }

    public Future<Void> chatStreamWithStringHandler(
            VolcesServiceMeta serviceMeta,
            JsonObject requestBody,
            Handler<String> handler,
            String requestId
    ) {
        requestBody.put("model", serviceMeta.getModel());
        Promise<Void> promise = Promise.promise();
        CutterOnString cutter = new CutterOnString();
        cutter.setComponentHandler(handler);
        requestBody.put("model", serviceMeta.getModel());
        serviceMeta.requestSSE(
                VolcesServiceMeta.pathOfV3ChatCompletions,
                requestBody,
                promise,
                cutter,
                requestId
        );
        return promise.future();
    }

    public Future<Void> chatStreamWithChunkHandler(
            VolcesServiceMeta serviceMeta,
            ChatCompletionsRequest requestBody,
            Handler<ChatCompletionsResponseChunk> handler,
            String requestId
    ) {
        return chatStreamWithStringHandler(
                serviceMeta,
                requestBody.toJsonObject(),
                s -> {
                    try {
                        var nakami = s.replaceFirst("^data:\\s*", "");
                        if (!Objects.equals("[DONE]", nakami)) {
                            JsonObject data = new JsonObject(nakami);
                            ChatCompletionsResponseChunk chunk = ChatCompletionsResponseChunk.wrap(data);
                            handler.handle(chunk);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                },
                requestId
        );
    }

    public Future<ChatCompletionsResponse> chatStreamWithBuffer(
            VolcesServiceMeta serviceMeta,
            ChatCompletionsRequest requestBody,
            String requestId
    ) {
        TempVolcesChatCompletionsResponse tempVolcesChatCompletionsResponse = new TempVolcesChatCompletionsResponse();
        return chatStreamWithChunkHandler(
                serviceMeta,
                requestBody,
                tempVolcesChatCompletionsResponse::accept,
                requestId
        )
                .compose(v -> {
                    ChatCompletionsResponse chatCompletionsResponse = tempVolcesChatCompletionsResponse.toChatCompletionsResponse();
                    return Future.succeededFuture(chatCompletionsResponse);
                });
    }

    public enum VolcesToolType {
        function
    }

    public interface ChatCompletionsRequest extends VolcesChatCompletionsRequestMixin<ChatCompletionsRequest> {
        static ChatCompletionsRequest create() {
            return new ChatCompletionsRequestImpl();
        }

        static ChatCompletionsRequest warp(JsonObject requestBody) {
            return new ChatCompletionsRequestImpl(requestBody);
        }
    }

    /**
     * 发出该消息的对话参与者的角色，可选 system, user 或 assistant。
     * role 参数的设定有助于模型理解对话的结构和上下文，从而生成更准确、更合适的回应。
     * 例如，在多轮对话中，role可以帮助模型追踪不同参与者的发言和意图，即使在复杂的对话场景中也能保持连贯性。
     */
    public enum ChatRole {
        system, user, assistant, tool
    }


    public interface MessageParam extends VolcesChatMessageParamMixin<MessageParam> {
        static MessageParam create() {
            return new MessageParamImpl();
        }

        static MessageParam wrap(JsonObject jsonObject) {
            return new MessageParamImpl(jsonObject);
        }
    }

    public interface MessageToolCall extends VolcesChatMessageToolCallParamMixin<MessageToolCall>, VolcesChatResponseFunctionCallMixin {
        static MessageToolCall create() {
            return new MessageToolCallImpl();
        }

        static MessageToolCall wrap(JsonObject jsonObject) {
            return new MessageToolCallImpl(jsonObject);
        }
    }

    public interface FunctionParam extends VolcesFunctionParamMixin {
        static FunctionParam wrap(JsonObject jsonObject) {
            return new FunctionParamImpl(jsonObject);
        }
    }

    public interface ToolParam extends VolcesToolParamMixin<ToolParam> {
        static ToolParam create(VolcesKit.FunctionDefinition functionDefinition) {
            return new ToolParamImpl(functionDefinition.toJsonObject());
        }

        static ToolParam create() {
            return new ToolParamImpl();
        }

        static ToolParam wrap(JsonObject jsonObject) {
            return new ToolParamImpl(jsonObject);
        }

        static VolcesKit.FunctionDefinition buildFunction(Handler<FunctionToolDefinition.FunctionToolDefinitionBuilder<VolcesFunctionToolDefinitionImpl.Builder, FunctionDefinition>> handler) {
            FunctionToolDefinition.FunctionToolDefinitionBuilder<VolcesFunctionToolDefinitionImpl.Builder, FunctionDefinition> builder = FunctionDefinition.builder();
            handler.handle(builder);
            return builder.build();
        }
    }

    public interface FunctionDefinition extends FunctionToolDefinition<FunctionDefinition> {
        static FunctionDefinition wrap(JsonObject jsonObject) {
            return new VolcesFunctionToolDefinitionImpl(jsonObject);
        }

        static FunctionToolDefinition.FunctionToolDefinitionBuilder<VolcesFunctionToolDefinitionImpl.Builder, VolcesKit.FunctionDefinition> builder() {
            return new VolcesFunctionToolDefinitionImpl.Builder();
        }
    }

    public interface ChatCompletionsResponse extends VolcesChatCompletionsResponseMixin {
        static VolcesKit.ChatCompletionsResponse wrap(JsonObject jsonObject) {
            return new ChatCompletionsResponseImpl(jsonObject);
        }

        interface Choice extends VolcesChatResponseChoiceMixin {
            static Choice wrap(JsonObject jsonObject) {
                return new ChatCompletionsResponseImpl.ChoiceImpl(jsonObject);
            }
        }

        interface Message extends VolcesChatResponseMessageMixin {
            static Message wrap(JsonObject jsonObject) {
                return new ChatCompletionsResponseImpl.MessageImpl(jsonObject);
            }

        }

        interface ToolCall extends VolcesChatResponseMessageToolCallMixin {
            static ToolCall wrap(JsonObject jsonObject) {
                return new ChatCompletionsResponseImpl.ToolCallImpl(jsonObject);
            }
        }
    }

    public interface Usage extends UnmodifiableJsonifiableEntity {
        static Usage wrap(JsonObject jsonObject) {
            return new UsageImpl(jsonObject);
        }

        /**
         * @return 输入的 prompt token 数量
         */
        default Integer getPromptTokens() {
            return readInteger("prompt_tokens");
        }

        /**
         * @return 模型生成的 token 数量
         */
        default Integer getCompletionTokens() {
            return readInteger("completion_tokens");
        }

        /**
         * @return 本次请求消耗的总 token 数量（输入 + 输出）
         */
        default Integer getTotalTokens() {
            return readInteger("total_tokens");
        }

    }

    public interface ChatCompletionsResponseChunk extends VolcesChatCompletionsResponseChunkMixin {
        static ChatCompletionsResponseChunk wrap(JsonObject jsonObject) {
            return new ChatCompletionsResponseChunkImpl(jsonObject);
        }
    }
}
