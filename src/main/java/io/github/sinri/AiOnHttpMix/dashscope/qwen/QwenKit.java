package io.github.sinri.AiOnHttpMix.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.MessageImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.chunk.ChatMessageResponseInChunkImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.chunk.ChatResponseChunkImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.chunk.TempOutput;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.embedding.TextEmbeddingGenerateRequestImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.embedding.TextEmbeddingGenerateResponseImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.request.QwenChatRequestImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.response.ChatMessageResponseImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.response.ChatTextResponseImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.tool.ToolCallImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.tool.ToolDefinitionImpl;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl.*;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.embedding.DashscopeTextEmbeddingGenerateRequestMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.embedding.DashscopeTextEmbeddingGenerateResponseMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.*;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.vl.*;
import io.github.sinri.keel.core.TechnicalPreview;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QwenKit {

    public Future<JsonObject> chat(
            DashscopeServiceMeta serviceMeta,
            JsonObject chatRequest,
            String requestId
    ) {
        return serviceMeta.callQwenTextGenerate(chatRequest, requestId);
    }

    public Future<ChatMessageResponse> chatForMessageResponse(
            DashscopeServiceMeta serviceMeta,
            ChatRequest chatRequest,
            String requestId
    ) {
        if (chatRequest.getParameters() == null) {
            chatRequest.setParameters(QwenChatRequestMixin.Parameters.create()
                    .setResultFormat(QwenChatRequestMixin.Parameters.ResultFormat.message)
            );
        } else {
            chatRequest.getParameters().setResultFormat(QwenChatRequestMixin.Parameters.ResultFormat.message);
        }
        return serviceMeta.callQwenTextGenerate(chatRequest.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    ChatMessageResponse chatMessageResponse = ChatMessageResponse.wrap(jsonObject);
                    return Future.succeededFuture(chatMessageResponse);
                });
    }

    public Future<Void> chatStreamWithStringHandler(
            DashscopeServiceMeta serviceMeta,
            JsonObject chatRequest,
            Handler<String> handler,
            String requestId
    ) {
        Promise<Void> promise = Promise.promise();
        CutterOnString cutter = new CutterOnString();
        cutter.setComponentHandler(handler);
        return serviceMeta.callQwenTextGenerateStream(
                chatRequest,
                promise,
                cutter,
                requestId
        );
    }

    public Future<Void> chatStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            ChatRequest chatRequest,
            Handler<ChatMessageResponseInChunk> handler,
            String requestId
    ) {
        return chatStreamWithStringHandler(
                serviceMeta,
                chatRequest.toJsonObject(),
                s -> {
                    ChatResponseChunk chatResponseChunk = ChatResponseChunk.parse(s);
                    String dataAsString = chatResponseChunk.getDataAsString();
                    ChatMessageResponseInChunk chatMessageResponseInChunk = ChatMessageResponseInChunk.parse(dataAsString);
                    handler.handle(chatMessageResponseInChunk);
                },
                requestId
        );
    }

    public Future<ChatMessageResponse> chatStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            ChatRequest chatRequest,
            String requestId
    ) {
        TempOutput tempOutput = new TempOutput();
        return chatStreamWithChunkHandler(
                serviceMeta,
                chatRequest,
                tempOutput::acceptChunkData,
                requestId
        )
                .compose(v -> {
                    return Future.succeededFuture(tempOutput.toChatMessageResponse());
                });
    }

    public Future<JsonObject> generateTextEmbedding(
            DashscopeServiceMeta serviceMeta,
            JsonObject requestBody,
            String requestId
    ) {
        return serviceMeta.callTextEmbeddingGeneration(requestBody, requestId);
    }

    public Future<TextEmbeddingGenerateResponse> generateTextEmbedding(
            DashscopeServiceMeta serviceMeta,
            TextEmbeddingGenerateRequest requestBody,
            String requestId
    ) {
        return serviceMeta.callTextEmbeddingGeneration(requestBody.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    return Future.succeededFuture(TextEmbeddingGenerateResponse.wrap(jsonObject));
                });
    }

    public enum TextEmbeddingModel {
        TEXT_EMBEDDING_V1("text-embedding-v1"),
        TEXT_EMBEDDING_V2("text-embedding-v2"),
        ;

        private final String modelCode;

        TextEmbeddingModel(String modelCode) {
            this.modelCode = modelCode;
        }

        public String getModelCode() {
            return modelCode;
        }

        public static TextEmbeddingModel fromModelCode(String modelCode) {
            for (TextEmbeddingModel textEmbeddingModel : TextEmbeddingModel.values()) {
                if (textEmbeddingModel.getModelCode().equals(modelCode)) {
                    return textEmbeddingModel;
                }
            }
            throw new IllegalArgumentException("Unknown model code: " + modelCode);
        }
    }

    /**
     * @see <a href="https://help.aliyun.com/zh/dashscope/developer-reference/model-introduction">模型概览</a>
     */
    public enum QwenModel {
        // 通义千问-Max: 通义千问系列效果最好的模型，适合复杂、多步骤的任务。
        QWEN_MAX("qwen-max"),
        QWEN_MAX_LONGCONTEXT("qwen-max-longcontext"),
        // 通义千问-Plus
        QWEN_PLUS("qwen-plus"),
        @TechnicalPreview(notice = "Beta阶段，上下文长度131072，最大输入128k")
        QWEN_PLUS_BETA("qwen-plus-0806"),
        // 通义千问-Turbo:通义千问系列速度最快、成本很低的模型，适合简单任务。
        QWEN_TURBO("qwen-turbo"),
        ;
        private final String modelCode;

        QwenModel(String modelCode) {
            this.modelCode = modelCode;
        }

        public String getModelCode() {
            return modelCode;
        }

        public static QwenModel fromModelCode(String modelCode) {
            return switch (modelCode) {
                case "qwen-max" -> QwenModel.QWEN_MAX;
                case "qwen-max-longcontext" -> QwenModel.QWEN_MAX_LONGCONTEXT;
                case "qwen-plus" -> QwenModel.QWEN_PLUS;
                case "qwen-plus-0806" -> QwenModel.QWEN_PLUS_BETA;
                case "qwen-turbo" -> QwenModel.QWEN_TURBO;
                default -> throw new IllegalArgumentException("Unknown modelCode: " + modelCode);
            };
        }

    }

    public interface TextEmbeddingGenerateRequest extends DashscopeTextEmbeddingGenerateRequestMixin<TextEmbeddingGenerateRequest> {
        static TextEmbeddingGenerateRequest create() {
            return new TextEmbeddingGenerateRequestImpl();
        }

        static TextEmbeddingGenerateRequest wrap(JsonObject requestBody) {
            return new TextEmbeddingGenerateRequestImpl(requestBody);
        }
    }

    public interface TextEmbeddingGenerateResponse extends DashscopeTextEmbeddingGenerateResponseMixin {
        static TextEmbeddingGenerateResponse wrap(JsonObject response) {
            return new TextEmbeddingGenerateResponseImpl(response);
        }
    }

    public interface ChatRequest extends QwenChatRequestMixin<ChatRequest> {
        static ChatRequest create() {
            return new QwenChatRequestImpl();
        }

        static ChatRequest wrap(JsonObject jsonObject) {
            return new QwenChatRequestImpl(jsonObject);
        }
    }


    public interface Message extends QwenMessageMixin<Message> {
        static Message create() {
            return new MessageImpl();
        }

        static Message wrap(JsonObject jsonObject) {
            return new MessageImpl(jsonObject);
        }
    }


    public interface ToolDefinition extends JsonifiableEntity<ToolDefinition> {

        /**
         * @param name        function的名称，必须是字母、数字，或包含下划线和短划线，最大长度为64。
         * @param description function的描述，供模型选择何时以及如何调用function。
         * @param parameters  表示function的参数描述，需要是一个合法的json schema。缺省表示function没有入参。
         */
        static ToolDefinition asFunction(
                @NotNull String name,
                @NotNull String description,
                @Nullable JsonObject parameters
        ) {
            return new ToolDefinitionImpl()
                    .setType(ToolType.function)
                    .setFunction(name, description, parameters);
        }

        static ToolDefinition asFunction(
                @NotNull String name,
                @NotNull String description,
                @Nullable List<FunctionArgument> parameters
        ) {
            return new ToolDefinitionImpl()
                    .setType(ToolType.function)
                    .setFunction(name, description, parameters);
        }

        /**
         * 表示tools的类型
         */
        enum ToolType {
            function
        }

        record FunctionArgument(
                String argumentName,
                String argumentType,
                String argumentDesc,
                boolean required
        ) {
        }
    }

    public interface ChatTextResponse extends QwenChatTextResponseMixin {
        static QwenKit.ChatTextResponse wrap(JsonObject jsonObject) {
            return new ChatTextResponseImpl(jsonObject);
        }
    }

    public interface ChatMessageResponse extends QwenChatMessageResponseMixin {
        static QwenKit.ChatMessageResponse wrap(JsonObject jsonObject) {
            return new ChatMessageResponseImpl(jsonObject);
        }
    }

    public interface ToolCall extends QwenToolCallMixin {
        static ToolCall wrap(JsonObject jsonObject) {
            return new ToolCallImpl(jsonObject);
        }
    }

    public interface ChatResponseChunk extends QwenChatResponseChunkMixin {
        static QwenKit.ChatResponseChunk parse(String string) {
            return new ChatResponseChunkImpl(string);
        }
    }

    public interface ChatMessageResponseInChunk extends QwenChatMessageResponseInChunkMixin {
        static ChatMessageResponseInChunk parse(String string) {
            return wrap(new JsonObject(string));
        }

        static ChatMessageResponseInChunk wrap(JsonObject jsonObject) {
            return new ChatMessageResponseInChunkImpl(jsonObject);
        }
    }

    public Future<JsonObject> chatVL(DashscopeServiceMeta serviceMeta, JsonObject jsonObject, String requestId) {
        return serviceMeta.callQwenMultiModalGenerate(jsonObject, requestId);
    }

    public Future<VLChatResponse> chatVL(DashscopeServiceMeta serviceMeta, VLChatRequest chatRequest, String requestId) {
        return serviceMeta.callQwenMultiModalGenerate(chatRequest.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    return Future.succeededFuture(VLChatResponse.wrap(jsonObject));
                });
    }

    public Future<Void> chatVLStreamWithStringHandler(
            DashscopeServiceMeta serviceMeta,
            JsonObject jsonObject,
            Handler<String> handler,
            String requestId
    ) {
        Promise<Void> promise = Promise.promise();
        CutterOnString cutterOnString = new CutterOnString();
        cutterOnString.setComponentHandler(handler);
        return serviceMeta.callQwenMultiModalGenerateStream(
                jsonObject,
                promise,
                cutterOnString,
                requestId
        );
    }

    public Future<Void> chatVLStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            VLChatRequest chatRequest,
            Handler<VLChatResponse> handler,
            String requestId
    ) {
        return this.chatVLStreamWithStringHandler(
                serviceMeta,
                chatRequest.toJsonObject(),
                s -> {
                    ChatResponseChunk responseChunk = ChatResponseChunk.parse(s);
                    String dataAsString = responseChunk.getDataAsString();
                    VLChatResponse vlChatResponse = VLChatResponse.wrap(new JsonObject(dataAsString));
                    handler.handle(vlChatResponse);
                },
                requestId
        );
    }

    public Future<VLChatResponse> chatVLStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            VLChatRequest chatRequest,
            String requestId
    ) {
        TempQwenVLChatResponse tempQwenVLChatResponse = new TempQwenVLChatResponse();
        return chatVLStreamWithChunkHandler(
                serviceMeta,
                chatRequest,
                tempQwenVLChatResponse::accept,
                requestId
        )
                .compose(v -> {
                    return Future.succeededFuture(tempQwenVLChatResponse.toVLChatResponse());
                });
    }

    public enum QwenVLModel {
        QWEN_VL_PLUS("qwen-vl-plus"),
        QWEN_VL_MAX("qwen-vl-max"),
        ;

        private final String modelCode;

        QwenVLModel(String modelCode) {
            this.modelCode = modelCode;
        }

        public String getModelCode() {
            return modelCode;
        }

        public static QwenVLModel fromModelCode(String modelCode) {
            return switch (modelCode) {
                case "qwen-vl-plus" -> QWEN_VL_PLUS;
                case "qwen-vl-max" -> QWEN_VL_MAX;
                default -> throw new IllegalArgumentException(modelCode);
            };
        }
    }

    public interface VLChatRequest extends QwenVLChatRequestMixin<VLChatRequest> {
        static VLChatRequest create() {
            return new VLChatRequestImpl();
        }
    }

    public interface VLChatInputMessage extends QwenVLChatInputMessageMixin<VLChatInputMessage> {
        static VLChatInputMessage create() {
            return new VLChatInputMessageImpl();
        }
    }

    public interface VLChatOutputMessage extends QwenVLChatOutputMessageMixin {
        static VLChatOutputMessage wrap(JsonObject jsonObject) {
            return new VLChatOutputMessageImpl(jsonObject);
        }
    }

    public interface VLChatResponse extends QwenVLChatResponseMixin {
        static VLChatResponse wrap(JsonObject jsonObject) {
            return new VLChatResponseImpl(jsonObject);
        }
    }

    public interface VLChatUsage extends QwenVLChatUsageMixin {
        static VLChatUsage wrap(JsonObject jsonObject) {
            return new VLChatUsageImpl(jsonObject);
        }
    }

}
