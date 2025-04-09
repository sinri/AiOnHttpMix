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
import io.github.sinri.keel.core.cutter.IntravenouslyCutter;
import io.github.sinri.keel.core.cutter.IntravenouslyCutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public final class QwenKit {

    public static Handler<String> getStreamBufferFragmentHandler(QwenStreamBuffer qwenStreamBuffer, String requestId) {
        return s -> {
            try {
                QwenResponseChunk chatMessageResponseInChunk = QwenResponseChunk.parse(s);
                qwenStreamBuffer.acceptChunkData(chatMessageResponseInChunk);
            } catch (Throwable e) {
                AigcMix.getVerboseLogger().exception(e, x -> x
                        .message("chunk handler exception in QwenKit.getStreamBufferFragmentHandler")
                        .context(j -> j.put("request_id", requestId)));
            }
        };
    }

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
            chatRequest.handleParameters(p -> p.setResultFormat(QwenRequest.Parameters.ResultFormat.message));
        } else {
            chatRequest.getParameters().setResultFormat(QwenRequest.Parameters.ResultFormat.message);
        }
        return serviceMeta.callQwenTextGenerate(chatRequest.toJsonObject(), requestId)
                          .compose(jsonObject -> {
                              QwenResponseInMessageFormat chatMessageResponse = QwenResponseInMessageFormat.wrap(200,
                                      jsonObject);
                              return Future.succeededFuture(chatMessageResponse);
                          }, throwable -> {
                              if (throwable instanceof ServiceMeta.AbnormalResponse abnormalResponse) {
                                  int statusCode = abnormalResponse.getStatusCode();
                                  JsonObject responseBodyAsJson = abnormalResponse.getResponseBodyAsJson();
                                  QwenResponseInMessageFormat chatMessageResponse =
                                          QwenResponseInMessageFormat.wrap(statusCode, responseBodyAsJson);
                                  return Future.succeededFuture(chatMessageResponse);
                              }
                              return Future.failedFuture(throwable);
                          });
    }

    public Future<Void> chatStreamWithStringHandler(
            DashscopeServiceMeta serviceMeta,
            JsonObject chatRequest,
            Handler<String> handler,
            int maxExecutionSeconds,
            String requestId
    ) {
        IntravenouslyCutter<String> cutter = new IntravenouslyCutterOnString(s -> {
            AigcMix.getVerboseLogger().debug(x -> x
                    .message("Component Handler in QwenKit.chatStreamWithStringHandler")
                    .context(j -> j
                            .put("component", s)
                            .put("request_id", requestId))
            );
            handler.handle(s);
            return Future.succeededFuture();
        });

        return serviceMeta.callQwenTextGenerateStream(
                chatRequest,
                cutter,
                maxExecutionSeconds,
                requestId
        );
    }

    public Future<Void> chatStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenRequest> chatRequestHandler,
            Handler<QwenResponseChunk> handler,
            int maxExecutionSeconds,
            String requestId
    ) {
        QwenRequest chatRequest = QwenRequest.create();
        chatRequest.handleParameters(p -> p.setIncrementalOutput(true));
        chatRequestHandler.handle(chatRequest);
        return chatStreamWithChunkHandler(serviceMeta, chatRequest, handler, maxExecutionSeconds, requestId);
    }

    /**
     * @since 1.1.1 Force use Message Format for response.
     */
    public Future<Void> chatStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            QwenRequest chatRequest,
            Handler<QwenResponseChunk> handler,
            int maxExecutionSeconds,
            String requestId
    ) {
        if (chatRequest.getParameters() == null) {
            chatRequest.handleParameters(p -> p.setResultFormat(QwenRequest.Parameters.ResultFormat.message));
        } else {
            chatRequest.getParameters().setResultFormat(QwenRequest.Parameters.ResultFormat.message);
        }
        return chatStreamWithStringHandler(
                serviceMeta,
                chatRequest.toJsonObject(),
                s -> {
                    QwenResponseFragment chatResponseChunk = QwenResponseFragment.parse(s);
                    String dataAsString = chatResponseChunk.getDataAsString();
                    QwenResponseChunk chatMessageResponseInChunk = QwenResponseChunk.parse(dataAsString);
                    handler.handle(chatMessageResponseInChunk);
                },
                maxExecutionSeconds,
                requestId
        );
    }

    public Future<QwenResponseInMessageFormat> chatStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenRequest> chatRequestHandler,
            int maxExecutionSeconds,
            String requestId
    ) {
        QwenRequest qwenRequest = QwenRequest.create();
        qwenRequest.handleParameters(p -> p.setIncrementalOutput(true));
        chatRequestHandler.handle(qwenRequest);
        return chatStreamWithBuffer(serviceMeta, qwenRequest, maxExecutionSeconds, requestId);
    }

    public Future<QwenResponseInMessageFormat> chatStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            QwenRequest chatRequest,
            int maxExecutionSeconds,
            String requestId
    ) {
        QwenStreamBuffer qwenStreamBuffer = new QwenStreamBuffer();
        QwenRequest.Parameters parameters = chatRequest.getParameters();
        if (parameters == null) {
            chatRequest.handleParameters(p -> p.setIncrementalOutput(true));
        } else {
            chatRequest.getParameters().setIncrementalOutput(true);
        }
        return chatStreamWithChunkHandler(
                serviceMeta,
                chatRequest,
                responseChunk -> {
                    AigcMix.getVerboseLogger().debug(x -> x
                            .message("io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit" +
                                    ".chatStreamWithBuffer::responseChunk")
                            .context(j -> j
                                    .put("raw", responseChunk.cloneAsJsonObject()))
                    );
                    qwenStreamBuffer.acceptChunkData(responseChunk);
                },
                maxExecutionSeconds,
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
                              return Future.succeededFuture(DashscopeTextEmbeddingGenerateResponse.wrap(200,
                                      jsonObject));
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

    public Future<QwenVLResponse> chatVL(DashscopeServiceMeta serviceMeta, QwenVLRequest chatRequest,
                                         String requestId) {
        return serviceMeta.callQwenMultiModalGenerate(chatRequest.toJsonObject(), requestId)
                          .compose(jsonObject -> {
                              return Future.succeededFuture(QwenVLResponse.wrap(jsonObject));
                          });
    }

    public Future<Void> chatVLStreamWithStringHandler(
            DashscopeServiceMeta serviceMeta,
            JsonObject jsonObject,
            Handler<String> handler,
            int maxExecutionSeconds,
            String requestId
    ) {
        IntravenouslyCutter<String> cutterOnString = new IntravenouslyCutterOnString(s -> {
            AigcMix.getVerboseLogger().debug(x -> x
                    .message("Component Handler in QwenKit.chatVLStreamWithStringHandler")
                    .context(j -> j
                            .put("component", s)
                            .put("request_id", requestId)
                    )
            );
            handler.handle(s);
            return Future.succeededFuture();
        });

        return serviceMeta.callQwenMultiModalGenerateStream(
                jsonObject,
                cutterOnString,
                maxExecutionSeconds,
                requestId
        );
    }

    public Future<Void> chatVLStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenVLRequest> chatRequestHandler,
            Handler<QwenVLResponse> handler,
            int maxExecutionSeconds,
            String requestId
    ) {
        QwenVLRequest request = QwenVLRequest.create();
        chatRequestHandler.handle(request);
        return chatVLStreamWithChunkHandler(serviceMeta, request, handler, maxExecutionSeconds, requestId);
    }

    public Future<Void> chatVLStreamWithChunkHandler(
            DashscopeServiceMeta serviceMeta,
            QwenVLRequest chatRequest,
            Handler<QwenVLResponse> handler,
            int maxExecutionSeconds,
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
                maxExecutionSeconds,
                requestId
        );
    }

    public Future<QwenVLResponse> chatVLStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            Handler<QwenVLRequest> chatRequestHandler,
            int maxExecutionSeconds,
            String requestId
    ) {
        QwenVLRequest request = QwenVLRequest.create();
        chatRequestHandler.handle(request);
        return chatVLStreamWithBuffer(serviceMeta, request, maxExecutionSeconds, requestId);
    }

    public Future<QwenVLResponse> chatVLStreamWithBuffer(
            DashscopeServiceMeta serviceMeta,
            QwenVLRequest chatRequest,
            int maxExecutionSeconds,
            String requestId
    ) {
        QwenVLStreamBuffer qwenVLStreamBuffer = new QwenVLStreamBuffer();
        QwenVLRequest.Parameters parameters = chatRequest.getParameters();
        if (parameters == null) {
            chatRequest.handleParameters(p -> p.setIncrementalOutput(true));
        } else {
            chatRequest.getParameters().setIncrementalOutput(true);
        }
        return chatVLStreamWithChunkHandler(
                serviceMeta,
                chatRequest,
                qwenVLStreamBuffer::accept,
                maxExecutionSeconds,
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

}
