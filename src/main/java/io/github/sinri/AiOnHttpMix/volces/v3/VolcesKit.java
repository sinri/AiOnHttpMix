package io.github.sinri.AiOnHttpMix.volces.v3;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunk;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunkString;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekStreamBuffer;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.v3.chunk.VolcesChatResponseChunk;
import io.github.sinri.AiOnHttpMix.volces.v3.chunk.VolcesChatStreamBuffer;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponse;
import io.github.sinri.keel.core.cutter.Cutter;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public final class VolcesKit {
    /**
     * @since 1.1.5
     */
    public static Handler<String> getStreamBufferFragmentHandler(
            VolcesChatStreamBuffer tempVolcesChatCompletionsResponse,
            String requestId
    ) {
        return s -> {
            AigcMix.getVerboseLogger()
                   .debug("io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit.getStreamBufferFragmentHandler::component | " + s);
            try {
                var nakami = s.replaceFirst("^data:\\s*", "");
                if (!Objects.equals("[DONE]", nakami)) {
                    JsonObject data = new JsonObject(nakami);
                    VolcesChatResponseChunk chunk = VolcesChatResponseChunk.wrap(data);
                    tempVolcesChatCompletionsResponse.accept(chunk);
                }
            } catch (Throwable e) {
                AigcMix.getVerboseLogger().exception(
                        e,
                        x -> x
                                .message("chunk handler exception in VolcesKit.chatStreamWithChunkHandler")
                                .context(j -> j.put("request_id", requestId))
                );
            }
        };
    }

    public Future<JsonObject> chat(VolcesServiceMeta serviceMeta, JsonObject requestBody, String requestId) {
        requestBody.put("model", serviceMeta.getModel());
        return serviceMeta.request(
                VolcesServiceMeta.pathOfV3ChatCompletions,
                requestBody,
                requestId
        );
    }

    public Future<VolcesChatResponse> chat(VolcesServiceMeta serviceMeta, Handler<VolcesChatRequest> requestBodyHandler, String requestId) {
        VolcesChatRequest request = VolcesChatRequest.create();
        requestBodyHandler.handle(request);
        return chat(serviceMeta, request, requestId);
    }

    public Future<VolcesChatResponse> chat(VolcesServiceMeta serviceMeta, VolcesChatRequest requestBody, String requestId) {
        requestBody.setModel(serviceMeta.getModel());
        return serviceMeta.request(
                                  VolcesServiceMeta.pathOfV3ChatCompletions,
                                  requestBody.toJsonObject(),
                                  requestId
                          )
                          .compose(resp -> {
                              return Future.succeededFuture(VolcesChatResponse.wrap(resp));
                          });
    }

    public Future<Void> chatStreamWithStringHandler(
            VolcesServiceMeta serviceMeta,
            JsonObject requestBody,
            Handler<String> handler,
            int maxExecutionSeconds,
            String requestId
    ) {
        requestBody.put("model", serviceMeta.getModel()).put("stream", true);
        Promise<Void> promise = Promise.promise();
        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            AigcMix.getVerboseLogger().debug(x -> x
                    .message("Component Handler in VolcesKit.chatStreamWithStringHandler")
                    .context(
                            j -> j
                                    .put("component", s)
                                    .put("request_id", requestId)
                    )
            );
            handler.handle(s);
        });
        requestBody.put("model", serviceMeta.getModel());
        serviceMeta.requestSSE(
                VolcesServiceMeta.pathOfV3ChatCompletions,
                requestBody,
                promise,
                cutter,
                maxExecutionSeconds,
                requestId
        );
        return promise.future();
    }

    public Future<Void> chatStreamWithChunkHandler(
            VolcesServiceMeta serviceMeta,
            Handler<VolcesChatRequest> requestBodyHandler,
            Handler<VolcesChatResponseChunk> handler,
            int maxExecutionSeconds,
            String requestId
    ) {
        VolcesChatRequest request = VolcesChatRequest.create();
        requestBodyHandler.handle(request);
        return chatStreamWithChunkHandler(serviceMeta, request, handler, maxExecutionSeconds, requestId);
    }

    public Future<Void> chatStreamWithChunkHandler(
            VolcesServiceMeta serviceMeta,
            VolcesChatRequest requestBody,
            Handler<VolcesChatResponseChunk> handler,
            int maxExecutionSeconds,
            String requestId
    ) {
        // requestBody.setStream(true);
        return chatStreamWithStringHandler(
                serviceMeta,
                requestBody.toJsonObject(),
                s -> {
                    try {
                        var nakami = s.replaceFirst("^data:\\s*", "");
                        if (!Objects.equals("[DONE]", nakami)) {
                            JsonObject data = new JsonObject(nakami);
                            VolcesChatResponseChunk chunk = VolcesChatResponseChunk.wrap(data);
                            handler.handle(chunk);
                        }
                    } catch (Throwable e) {
                        AigcMix.getVerboseLogger().exception(
                                e,
                                x -> x.message("chunk handler exception in VolcesKit.chatStreamWithChunkHandler")
                                      .context(j -> j.put("request_id", requestId))
                        );
                    }
                },
                maxExecutionSeconds,
                requestId
        );
    }

    public Future<VolcesChatResponse> chatStreamWithBuffer(
            VolcesServiceMeta serviceMeta,
            Handler<VolcesChatRequest> requestBodyHandler,
            int maxExecutionSeconds,
            String requestId
    ) {
        VolcesChatRequest request = VolcesChatRequest.create();
        requestBodyHandler.handle(request);
        return chatStreamWithBuffer(serviceMeta, request, maxExecutionSeconds, requestId);
    }

    public Future<VolcesChatResponse> chatStreamWithBuffer(
            VolcesServiceMeta serviceMeta,
            VolcesChatRequest requestBody,
            int maxExecutionSeconds,
            String requestId
    ) {
        VolcesChatStreamBuffer tempVolcesChatCompletionsResponse = new VolcesChatStreamBuffer();
        return chatStreamWithChunkHandler(
                serviceMeta,
                requestBody,
                tempVolcesChatCompletionsResponse::accept,
                maxExecutionSeconds,
                requestId
        )
                .compose(v -> {
                    VolcesChatResponse chatCompletionsResponse = tempVolcesChatCompletionsResponse.toChatCompletionsResponse();
                    return Future.succeededFuture(chatCompletionsResponse);
                });
    }

    /**
     * @since 1.2.2
     */
    public Future<JsonObject> chatForDeepSeekV3(
            VolcesServiceMeta serviceMeta,
            JsonObject requestBody,
            String requestId
    ) {
        requestBody.put("model", serviceMeta.getModel());
        return serviceMeta.request(VolcesServiceMeta.pathOfV3ChatCompletions, requestBody, requestId);
    }

    /**
     * @since 1.2.2
     */
    public Future<DeepseekChatResponse> chatForDeepSeekV3(
            VolcesServiceMeta serviceMeta,
            DeepseekChatRequest request,
            String requestId
    ) {
        request.setModel(serviceMeta.getModel());
        return serviceMeta.request(VolcesServiceMeta.pathOfV3ChatCompletions, request.toJsonObject(), requestId)
                          .compose(resp -> {
                              return Future.succeededFuture(DeepseekChatResponse.wrap(resp));
                          });
    }

    /**
     * @since 1.2.2
     */
    public Future<Void> chatStreamWithChunkHandlerForDeepSeekV3(VolcesServiceMeta serviceMeta,
                                                                DeepseekChatRequest request,
                                                                Handler<DeepseekResponseChunk> chunkHandler,
                                                                int maxExecutionSeconds,
                                                                String requestId
    ) {
        request.setModel(serviceMeta.getModel());
        request.setStream(true);
        Promise<Void> promise = Promise.promise();

        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(component -> {
            DeepseekResponseChunkString deepseekResponseChunkString = new DeepseekResponseChunkString(component);
            if (!deepseekResponseChunkString.isDoneChunk() && !deepseekResponseChunkString.isKeepAliveChunk()) {
                DeepseekResponseChunk chunk = deepseekResponseChunkString.getChunk();
                if (chunk != null) {
                    chunkHandler.handle(chunk);
                }
            }
        });
        serviceMeta.requestSSE(VolcesServiceMeta.pathOfV3ChatCompletions, request.toJsonObject(), promise, cutter, maxExecutionSeconds, requestId);
        return promise.future();
    }

    /**
     * @since 1.2.2
     */
    public Future<DeepseekChatResponse> chatSSEWithBufferForDeepSeekV3(VolcesServiceMeta serviceMeta,
                                                                       DeepseekChatRequest request,
                                                                       int maxExecutionSeconds,
                                                                       String requestId
    ) {
        DeepseekStreamBuffer streamBuffer = new DeepseekStreamBuffer();
        return chatStreamWithChunkHandlerForDeepSeekV3(serviceMeta, request, streamBuffer::accept, maxExecutionSeconds, requestId)
                .compose(v -> {
                    return Future.succeededFuture(streamBuffer.toResponse());
                });
    }
}
