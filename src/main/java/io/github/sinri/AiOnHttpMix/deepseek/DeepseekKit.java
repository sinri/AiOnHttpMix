package io.github.sinri.AiOnHttpMix.deepseek;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunk;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunkString;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekStreamBuffer;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import io.github.sinri.keel.core.cutter.Cutter;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

/**
 * DeepSeek的模型，目前并没有非常稳定，树大招风。
 * 按照20250205的情报，官方API已被攻陷，火山引擎的不支持FC。
 */
public class DeepseekKit {
    public static Handler<String> getStreamBufferFragmentHandler(DeepseekStreamBuffer buffer, String requestId) {
        return s -> {
            AigcMix.getVerboseLogger().debug(x -> x
                    .message("io.github.sinri.AiOnHttpMix.deepseek.DeepseekClient.getStreamBufferFragmentHandler::component | " + s)
                    .context("request_id", requestId)
            );

            JsonObject jsonObject = new JsonObject(s);
            DeepseekResponseChunk chunk = new DeepseekResponseChunk(jsonObject);
            buffer.accept(chunk);

//            AigcMix.getVerboseLogger().debug("io.github.sinri.AiOnHttpMix.deepseek.DeepseekClient.getStreamBufferFragmentHandler::component | " + s);
//            try {
//                var nakami = s.replaceFirst("^data:\\s*", "");
//                if (!Objects.equals("[DONE]", nakami)) {
//                    JsonObject data = new JsonObject(nakami);
//                    VolcesChatResponseChunk chunk = VolcesChatResponseChunk.wrap(data);
//                    tempVolcesChatCompletionsResponse.accept(chunk);
//                }
//            } catch (Throwable e) {
//                AigcMix.getVerboseLogger().exception(
//                        e,
//                        "chunk handler exception in VolcesKit.chatStreamWithChunkHandler",
//                        j -> j.put("request_id", requestId)
//                );
//            }
        };
    }

    public Future<JsonObject> chat(
            VolcesServiceMeta serviceMeta,
            JsonObject requestBody,
            String requestId
    ) {
        requestBody.put("model", serviceMeta.getModel());
        return serviceMeta.request(VolcesServiceMeta.pathOfV3ChatCompletions, requestBody, requestId);
    }

    public Future<DeepseekChatResponse> chat(
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

    public Future<Void> chatStreamWithChunkHandler(VolcesServiceMeta serviceMeta,
                                                   DeepseekChatRequest request,
                                                   Handler<DeepseekResponseChunk> chunkHandler,
                                                   String requestId
    ) {
        request.setModel(serviceMeta.getModel());
        request.setStream(true);
        Promise<Void> promise = Promise.promise();

        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(component -> {
//            System.out.println("[COMPONENT] " + component);
            DeepseekResponseChunkString deepseekResponseChunkString = new DeepseekResponseChunkString(component);
            if (!deepseekResponseChunkString.isDoneChunk() && !deepseekResponseChunkString.isKeepAliveChunk()) {
                DeepseekResponseChunk chunk = deepseekResponseChunkString.getChunk();
                if (chunk != null) {
                    chunkHandler.handle(chunk);
                }
            }
        });
        serviceMeta.requestSSE(VolcesServiceMeta.pathOfV3ChatCompletions, request.toJsonObject(), promise, cutter, requestId);
        return promise.future();
    }

    public Future<DeepseekChatResponse> chatSSEWithBuffer(VolcesServiceMeta serviceMeta,
                                                          DeepseekChatRequest request,
                                                          String requestId
    ) {
        DeepseekStreamBuffer streamBuffer = new DeepseekStreamBuffer();
        return chatStreamWithChunkHandler(serviceMeta, request, streamBuffer::accept, requestId)
                .compose(v -> {
                    return Future.succeededFuture(streamBuffer.toResponse());
                });
    }

    public Future<JsonObject> chat(
            DeepseekServiceMeta serviceMeta,
            JsonObject requestBody,
            String requestId
    ) {
        return serviceMeta.request("/chat/completions", requestBody, requestId);
    }

    public Future<DeepseekChatResponse> chat(
            DeepseekServiceMeta serviceMeta,
            DeepseekChatRequest request,
            String requestId
    ) {
        return serviceMeta.request("/chat/completions", request.toJsonObject(), requestId)
                .compose(resp -> {
                    // System.out.println("io.github.sinri.AiOnHttpMix.deepseek.DeepseekKit.chat(io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta, io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest, java.lang.String):\n"+resp.toString());
                    return Future.succeededFuture(DeepseekChatResponse.wrap(resp));
                });
    }

    public Future<Void> chatStreamWithStreamHandler(
            DeepseekServiceMeta serviceMeta,
            JsonObject requestBody,
            Handler<String> streamHandler,
            String requestId
    ) {
        requestBody.put("stream", true);

        Promise<Void> promise = Promise.promise();

        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(streamHandler);

        serviceMeta.requestSSE(
                "/chat/completions",
                requestBody,
                promise,
                cutter,
                requestId
        );
        return promise.future();
    }

    public Future<DeepseekChatResponse> chatStreamWithBuffer(
            DeepseekServiceMeta serviceMeta,
            DeepseekChatRequest request,
            String requestId
    ) {
        DeepseekStreamBuffer streamBuffer = new DeepseekStreamBuffer();
        Handler<String> streamHandler = s -> {
            DeepseekResponseChunkString deepseekResponseChunkString = new DeepseekResponseChunkString(s);
            if (!deepseekResponseChunkString.isDoneChunk()) {
                if (!deepseekResponseChunkString.isKeepAliveChunk()) {
                    try {
                        DeepseekResponseChunk chunk = deepseekResponseChunkString.getChunk();
                        streamBuffer.accept(chunk);
                    } catch (Throwable throwable) {
                        // ignore it
                        throwable.printStackTrace();
                    }
                }
            } else {
                streamBuffer.meetDoneFlag();
            }
        };
        return this.chatStreamWithStreamHandler(serviceMeta, request.toJsonObject(), streamHandler, requestId)
                .compose(v -> {
                    if (!streamBuffer.isMetDoneFlag()) {
                        return Keel.asyncSleep(500L);
                    }
                    return Future.succeededFuture();
                })
                .compose(v -> {
                    if (streamBuffer.isMetDoneFlag()) {
                        DeepseekChatResponse response = streamBuffer.toResponse();
                        return Future.succeededFuture(response);
                    } else {
                        return Future.failedFuture("Stream buffer did not met DONE flag. The cached buffer is " + streamBuffer.toResponse().toString());
                    }
                });
    }
}
