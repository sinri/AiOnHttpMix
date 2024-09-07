package io.github.sinri.AiOnHttpMix.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding.DashscopeTextEmbeddingGenerateRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding.DashscopeTextEmbeddingGenerateResponse;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseFragment;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenStreamBuffer;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLResponse;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLStreamBuffer;
import io.github.sinri.AiOnHttpMix.utils.ServiceMeta;
import io.github.sinri.keel.core.TechnicalPreview;
import io.github.sinri.keel.core.cutter.Cutter;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public final class QwenKit {

    public Future<JsonObject> chat(
            DashscopeServiceMeta serviceMeta,
            JsonObject chatRequest,
            String requestId
    ) {
        return serviceMeta.callQwenTextGenerate(chatRequest, requestId);
    }

    public Future<QwenResponseInMessageFormat> chatForMessageResponse(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenRequest> chatRequestHandler,
            String requestId
    ) {
        QwenRequest chatRequest = QwenRequest.create();
        chatRequestHandler.handle(chatRequest);
        return chatForMessageResponse(serviceMeta, chatRequest, requestId);
    }

    public Future<QwenResponseInMessageFormat> chatForMessageResponse(
            DashscopeServiceMeta serviceMeta,
            QwenRequest chatRequest,
            String requestId
    ) {
        if (chatRequest.getParameters() == null) {
            chatRequest.setParameters(QwenRequest.Parameters.create()
                    .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
            );
        } else {
            chatRequest.getParameters().setResultFormat(QwenRequest.Parameters.ResultFormat.message);
        }
        return serviceMeta.callQwenTextGenerate(chatRequest.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    QwenResponseInMessageFormat chatMessageResponse = QwenResponseInMessageFormat.wrap(200, jsonObject);
                    return Future.succeededFuture(chatMessageResponse);
                }, throwable -> {
                    if (throwable instanceof ServiceMeta.AbnormalResponse abnormalResponse) {
                        int statusCode = abnormalResponse.getStatusCode();
                        JsonObject responseBodyAsJson = abnormalResponse.getResponseBodyAsJson();
                        QwenResponseInMessageFormat chatMessageResponse = QwenResponseInMessageFormat.wrap(statusCode, responseBodyAsJson);
                        return Future.succeededFuture(chatMessageResponse);
                    }
                    return Future.failedFuture(throwable);
                });
    }

    public Future<Void> chatStreamWithStringHandler(
            DashscopeServiceMeta serviceMeta,
            JsonObject chatRequest,
            Handler<String> handler,
            String requestId
    ) {
        Promise<Void> promise = Promise.promise();
        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            AigcMix.getVerboseLogger().debug(
                    "Component Handler in QwenKit.chatStreamWithStringHandler",
                    j -> j
                            .put("component", s)
                            .put("request_id", requestId)
            );
            handler.handle(s);
        });
        return serviceMeta.callQwenTextGenerateStream(
                chatRequest,
                promise,
                cutter,
                requestId
        );
    }

    public Future<Void> chatStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenRequest> chatRequestHandler,
            Handler<QwenResponseChunk> handler,
            String requestId
    ) {
        QwenRequest chatRequest = QwenRequest.create();
        chatRequestHandler.handle(chatRequest);
        return chatStreamWithChunkHandler(serviceMeta, chatRequest, handler, requestId);
    }

    public Future<Void> chatStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            QwenRequest chatRequest,
            Handler<QwenResponseChunk> handler,
            String requestId
    ) {
        return chatStreamWithStringHandler(
                serviceMeta,
                chatRequest.toJsonObject(),
                s -> {
                    QwenResponseFragment chatResponseChunk = QwenResponseFragment.parse(s);
                    String dataAsString = chatResponseChunk.getDataAsString();
                    QwenResponseChunk chatMessageResponseInChunk = QwenResponseChunk.parse(dataAsString);
                    handler.handle(chatMessageResponseInChunk);
                },
                requestId
        );
    }

    public Future<QwenResponseInMessageFormat> chatStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenRequest> chatRequestHandler,
            String requestId
    ) {
        QwenRequest qwenRequest = QwenRequest.create();
        chatRequestHandler.handle(qwenRequest);
        return chatStreamWithBuffer(serviceMeta, qwenRequest, requestId);
    }

    public Future<QwenResponseInMessageFormat> chatStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            QwenRequest chatRequest,
            String requestId
    ) {
        QwenStreamBuffer qwenStreamBuffer = new QwenStreamBuffer();
        return chatStreamWithChunkHandler(
                serviceMeta,
                chatRequest,
                qwenStreamBuffer::acceptChunkData,
                requestId
        )
                .compose(v -> {
                    return Future.succeededFuture(qwenStreamBuffer.toChatMessageResponse());
                });
    }

    public Future<JsonObject> generateTextEmbedding(
            DashscopeServiceMeta serviceMeta,
            JsonObject requestBody,
            String requestId
    ) {
        return serviceMeta.callTextEmbeddingGeneration(requestBody, requestId);
    }

    public Future<DashscopeTextEmbeddingGenerateResponse> generateTextEmbedding(
            DashscopeServiceMeta serviceMeta,
            Handler<DashscopeTextEmbeddingGenerateRequest> requestBodyHandler,
            String requestId
    ) {
        DashscopeTextEmbeddingGenerateRequest request = DashscopeTextEmbeddingGenerateRequest.create();
        requestBodyHandler.handle(request);
        return generateTextEmbedding(serviceMeta, request, requestId);
    }

    public Future<DashscopeTextEmbeddingGenerateResponse> generateTextEmbedding(
            DashscopeServiceMeta serviceMeta,
            DashscopeTextEmbeddingGenerateRequest requestBody,
            String requestId
    ) {
        return serviceMeta.callTextEmbeddingGeneration(requestBody.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    return Future.succeededFuture(DashscopeTextEmbeddingGenerateResponse.wrap(200, jsonObject));
                }, throwable -> {
                    if (throwable instanceof ServiceMeta.AbnormalResponse abnormalResponse) {
                        int statusCode = abnormalResponse.getStatusCode();
                        JsonObject responseBodyAsJson = abnormalResponse.getResponseBodyAsJson();
                        return Future.succeededFuture(DashscopeTextEmbeddingGenerateResponse.wrap(statusCode, responseBodyAsJson));
                    }
                    return Future.failedFuture(throwable);
                });
    }

    public Future<JsonObject> chatVL(DashscopeServiceMeta serviceMeta, JsonObject jsonObject, String requestId) {
        return serviceMeta.callQwenMultiModalGenerate(jsonObject, requestId);
    }

    public Future<QwenVLResponse> chatVL(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenVLRequest> chatRequestHandler,
            String requestId
    ) {
        QwenVLRequest request = QwenVLRequest.create();
        chatRequestHandler.handle(request);
        return chatVL(serviceMeta, request, requestId);
    }

    public Future<QwenVLResponse> chatVL(DashscopeServiceMeta serviceMeta, QwenVLRequest chatRequest, String requestId) {
        return serviceMeta.callQwenMultiModalGenerate(chatRequest.toJsonObject(), requestId)
                .compose(jsonObject -> {
                    return Future.succeededFuture(QwenVLResponse.wrap(jsonObject));
                });
    }

    public Future<Void> chatVLStreamWithStringHandler(
            DashscopeServiceMeta serviceMeta,
            JsonObject jsonObject,
            Handler<String> handler,
            String requestId
    ) {
        Promise<Void> promise = Promise.promise();
        Cutter<String> cutterOnString = new CutterOnString();
        cutterOnString.setComponentHandler(s -> {
            AigcMix.getVerboseLogger().debug(
                    "Component Handler in QwenKit.chatVLStreamWithStringHandler",
                    j -> j
                            .put("component", s)
                            .put("request_id", requestId)
            );
            handler.handle(s);
        });
        return serviceMeta.callQwenMultiModalGenerateStream(
                jsonObject,
                promise,
                cutterOnString,
                requestId
        );
    }

    public Future<Void> chatVLStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenVLRequest> chatRequestHandler,
            Handler<QwenVLResponse> handler,
            String requestId
    ) {
        QwenVLRequest request = QwenVLRequest.create();
        chatRequestHandler.handle(request);
        return chatVLStreamWithChunkHandler(serviceMeta, request, handler, requestId);
    }

    public Future<Void> chatVLStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            QwenVLRequest chatRequest,
            Handler<QwenVLResponse> handler,
            String requestId
    ) {
        return this.chatVLStreamWithStringHandler(
                serviceMeta,
                chatRequest.toJsonObject(),
                s -> {
                    QwenResponseFragment responseChunk = QwenResponseFragment.parse(s);
                    String dataAsString = responseChunk.getDataAsString();
                    QwenVLResponse vlChatResponse = QwenVLResponse.wrap(new JsonObject(dataAsString));
                    handler.handle(vlChatResponse);
                },
                requestId
        );
    }

    public Future<QwenVLResponse> chatVLStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenVLRequest> chatRequestHandler,
            String requestId
    ) {
        QwenVLRequest request = QwenVLRequest.create();
        chatRequestHandler.handle(request);
        return chatVLStreamWithBuffer(serviceMeta, request, requestId);
    }

    public Future<QwenVLResponse> chatVLStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            QwenVLRequest chatRequest,
            String requestId
    ) {
        QwenVLStreamBuffer qwenVLStreamBuffer = new QwenVLStreamBuffer();
        return chatVLStreamWithChunkHandler(
                serviceMeta,
                chatRequest,
                qwenVLStreamBuffer::accept,
                requestId
        )
                .compose(v -> {
                    return Future.succeededFuture(qwenVLStreamBuffer.toVLChatResponse());
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

        public static TextEmbeddingModel fromModelCode(String modelCode) {
            for (TextEmbeddingModel textEmbeddingModel : TextEmbeddingModel.values()) {
                if (textEmbeddingModel.getModelCode().equals(modelCode)) {
                    return textEmbeddingModel;
                }
            }
            throw new IllegalArgumentException("Unknown model code: " + modelCode);
        }

        public String getModelCode() {
            return modelCode;
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

        public String getModelCode() {
            return modelCode;
        }

    }

    public enum QwenVLModel {
        QWEN_VL_PLUS("qwen-vl-plus"),
        QWEN_VL_MAX("qwen-vl-max"),
        ;

        private final String modelCode;

        QwenVLModel(String modelCode) {
            this.modelCode = modelCode;
        }

        public static QwenVLModel fromModelCode(String modelCode) {
            return switch (modelCode) {
                case "qwen-vl-plus" -> QWEN_VL_PLUS;
                case "qwen-vl-max" -> QWEN_VL_MAX;
                default -> throw new IllegalArgumentException(modelCode);
            };
        }

        public String getModelCode() {
            return modelCode;
        }
    }

}
