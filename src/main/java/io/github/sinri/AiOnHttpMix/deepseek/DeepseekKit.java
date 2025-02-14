package io.github.sinri.AiOnHttpMix.deepseek;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunk;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunkString;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekStreamBuffer;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.github.sinri.keel.core.cutter.Cutter;
import io.github.sinri.keel.core.cutter.CutterOnString;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DeepSeek的模型，目前并没有非常稳定，树大招风。 按照20250205的情报，官方API已被攻陷，火山引擎的不支持FC。
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
        };
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
            int maxExecutionSeconds,
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
                maxExecutionSeconds,
                requestId
        );
        return promise.future();
    }

    /**
     * @since 1.2.2
     */
    public Future<Void> chatStreamWithChunkHandler(
            DeepseekServiceMeta serviceMeta,
            DeepseekChatRequest request,
            Handler<DeepseekResponseChunk> chunkHandler,
            int maxExecutionSeconds,
            String requestId
    ) {
        request.setStream(true);

        Promise<Void> promise = Promise.promise();

        AtomicBoolean doneRef = new AtomicBoolean(false);
        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            DeepseekResponseChunkString deepseekResponseChunkString = new DeepseekResponseChunkString(s);
            if (!deepseekResponseChunkString.isDoneChunk() && !deepseekResponseChunkString.isKeepAliveChunk()) {
                DeepseekResponseChunk chunk = deepseekResponseChunkString.getChunk();
                chunkHandler.handle(chunk);
            } else {
                if (deepseekResponseChunkString.isDoneChunk()) {
                    AigcMix.getVerboseLogger().debug("DONE CHUNK MET");
                    doneRef.set(true);
                }
                if (deepseekResponseChunkString.isKeepAliveChunk()) {
                    AigcMix.getVerboseLogger().debug("KEEPALIVE CHUNK MET");
                }
            }
        });

        serviceMeta.requestSSE(
                "/chat/completions",
                request.toJsonObject(),
                promise,
                cutter,
                maxExecutionSeconds,
                requestId
        );
        return promise.future()
                      .compose(v -> {
                          if (!doneRef.get()) {
                              return Future.failedFuture(new Exception(
                                      "DeepSeek did not accepted this request, maybe lack of resource."
                              ));
                          }
                          return Future.succeededFuture();
                      });
    }

    public Future<DeepseekChatResponse> chatStreamWithBuffer(
            DeepseekServiceMeta serviceMeta,
            DeepseekChatRequest request,
            int maxExecutionSeconds,
            String requestId
    ) {
        DeepseekStreamBuffer streamBuffer = new DeepseekStreamBuffer();

        return this.chatStreamWithChunkHandler(
                           serviceMeta,
                           request,
                           streamBuffer::accept,
                           maxExecutionSeconds,
                           requestId
                   )
                   .compose(v -> {
                       if (streamBuffer.isMetDoneFlag()) {
                           DeepseekChatResponse response = streamBuffer.toResponse();
                           AigcMix.getVerboseLogger().debug("DeepseekChatResponse: " + response.cloneAsJsonObject());
                           return Future.succeededFuture(response);
                       } else {
                           return Future.failedFuture("Stream buffer did not met DONE flag. The cached buffer is " + streamBuffer.toResponse()
                                                                                                                                 .toString());
                       }
                   });

        //        Handler<String> streamHandler = s -> {
        //            DeepseekResponseChunkString deepseekResponseChunkString = new DeepseekResponseChunkString(s);
        //            if (!deepseekResponseChunkString.isDoneChunk()) {
        //                if (!deepseekResponseChunkString.isKeepAliveChunk()) {
        //                    try {
        //                        DeepseekResponseChunk chunk = deepseekResponseChunkString.getChunk();
        //                        streamBuffer.accept(chunk);
        //                    } catch (Throwable throwable) {
        //                        // ignore it
        //                        AigcMix.getVerboseLogger().exception(throwable,
        //                                "io.github.sinri.AiOnHttpMix.deepseek.DeepseekKit.chatStreamWithBuffer met error",
        //                                context -> context.put("request_id", requestId).put("chunk_string", s)
        //                        );
        //                        throw new RuntimeException(throwable);
        //                    }
        //                }
        //            } else {
        //                streamBuffer.meetDoneFlag();
        //            }
        //        };
        //        return this.chatStreamWithStreamHandler(serviceMeta, request.toJsonObject(), streamHandler, maxExecutionSeconds, requestId)
        //                   .compose(v -> {
        //                       if (!streamBuffer.isMetDoneFlag()) {
        //                           return Keel.asyncSleep(500L);
        //                       }
        //                       return Future.succeededFuture();
        //                   })
        //                   .compose(v -> {
        //                       if (streamBuffer.isMetDoneFlag()) {
        //                           DeepseekChatResponse response = streamBuffer.toResponse();
        //                           return Future.succeededFuture(response);
        //                       } else {
        //                           return Future.failedFuture("Stream buffer did not met DONE flag. The cached buffer is " + streamBuffer.toResponse()
        //                                                                                                                                 .toString());
        //                       }
        //                   });
    }
}
