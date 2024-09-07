package io.github.sinri.AiOnHttpMix.volces.v3;

import io.github.sinri.AiOnHttpMix.AigcMix;
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
            String requestId
    ) {
        requestBody.put("model", serviceMeta.getModel());
        Promise<Void> promise = Promise.promise();
        Cutter<String> cutter = new CutterOnString();
        cutter.setComponentHandler(s -> {
            AigcMix.getVerboseLogger().debug(
                    "Component Handler in VolcesKit.chatStreamWithStringHandler",
                    j -> j
                            .put("component", s)
                            .put("request_id", requestId)
            );
            handler.handle(s);
        });
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
            Handler<VolcesChatRequest> requestBodyHandler,
            Handler<VolcesChatResponseChunk> handler,
            String requestId
    ) {
        VolcesChatRequest request = VolcesChatRequest.create();
        requestBodyHandler.handle(request);
        return chatStreamWithChunkHandler(serviceMeta, request, handler, requestId);
    }

    public Future<Void> chatStreamWithChunkHandler(
            VolcesServiceMeta serviceMeta,
            VolcesChatRequest requestBody,
            Handler<VolcesChatResponseChunk> handler,
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
                            VolcesChatResponseChunk chunk = VolcesChatResponseChunk.wrap(data);
                            handler.handle(chunk);
                        }
                    } catch (Throwable e) {
                        AigcMix.getVerboseLogger().exception(
                                e,
                                "chunk handler exception in VolcesKit.chatStreamWithChunkHandler",
                                j -> j.put("request_id", requestId)
                        );
                    }
                },
                requestId
        );
    }

    public Future<VolcesChatResponse> chatStreamWithBuffer(
            VolcesServiceMeta serviceMeta,
            Handler<VolcesChatRequest> requestBodyHandler,
            String requestId
    ) {
        VolcesChatRequest request = VolcesChatRequest.create();
        requestBodyHandler.handle(request);
        return chatStreamWithBuffer(serviceMeta, request, requestId);
    }

    public Future<VolcesChatResponse> chatStreamWithBuffer(
            VolcesServiceMeta serviceMeta,
            VolcesChatRequest requestBody,
            String requestId
    ) {
        VolcesChatStreamBuffer tempVolcesChatCompletionsResponse = new VolcesChatStreamBuffer();
        return chatStreamWithChunkHandler(
                serviceMeta,
                requestBody,
                tempVolcesChatCompletionsResponse::accept,
                requestId
        )
                .compose(v -> {
                    VolcesChatResponse chatCompletionsResponse = tempVolcesChatCompletionsResponse.toChatCompletionsResponse();
                    return Future.succeededFuture(chatCompletionsResponse);
                });
    }
}
