package io.github.sinri.AiOnHttpMix.provider.volces;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseFragment;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponse;
import io.github.sinri.AiOnHttpMix.utils.ServiceKit;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

public class VolcesKit implements ServiceKit<DoubaoRequest, DoubaoResponse, DoubaoResponseChunk> {
    private final VolcesServiceAdapter serviceAdapter;

    public VolcesKit(VolcesServiceAdapter serviceAdapter) {
        this.serviceAdapter = serviceAdapter;
    }

    public static Future<DoubaoResponseChunk> parseStreamFragmentToChunk(String fragment) {
        DoubaoResponseFragment f = DoubaoResponseFragment.wrap(fragment);
        JsonObject data = f.getData();
        if (data != null) {
            DoubaoResponseChunk c = DoubaoResponseChunk.wrap(data);
            return Future.succeededFuture(c);
        } else {
            AigcMix.getVerboseLogger().warning("in fragment data is null");
            return Future.succeededFuture(null);
        }
    }

    public static Future<Void> handleStreamFragment(String fragment, Function<DoubaoResponseChunk, Future<Void>> chunkProcessor) {
        return Future.succeededFuture()
                     .compose(v -> {
                         return parseStreamFragmentToChunk(fragment);
                     })
                     .compose(chunk -> {
                         if (chunk == null) {
                             AigcMix.getVerboseLogger().warning("in fragment data is null");
                             return Future.succeededFuture();
                         } else {
                             return chunkProcessor.apply(chunk);
                         }
                     })
                     .onFailure(throwable -> {
                         AigcMix.getVerboseLogger().exception(throwable);
                     });
    }

    @Override
    public VolcesServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    public Future<JsonObject> chat(
            ChatModel chatModel,
            JsonObject rawRequest,
            String requestId
    ) {
        return serviceAdapter.request(chatModel, rawRequest, requestId);
    }

    public Future<DoubaoResponse> chat(
            ChatModel chatModel,
            DoubaoRequest request,
            String requestId
    ) {
        return serviceAdapter.request(chatModel, request.toJsonObject(), requestId)
                             .compose(rawResponse -> Future.succeededFuture(DoubaoResponse.wrap(rawResponse)));
    }

    public Future<Void> chatStream(
            ChatModel chatModel,
            JsonObject rawRequest,
            Function<String, Future<Void>> fragmentProcessor,
            long cutterTimeout,
            String requestId
    ) {
        rawRequest.put("stream", true);
        return serviceAdapter.requestStream(chatModel, rawRequest, fragmentProcessor, cutterTimeout, requestId);
    }

    public Future<Void> chatStream(
            ChatModel chatModel,
            DoubaoRequest request,
            Function<DoubaoResponseChunk, Future<Void>> chunkProcessor,
            long cutterTimeout,
            String requestId
    ) {
        request.stream(true);
        return serviceAdapter.requestStream(chatModel, request.toJsonObject(), fragment -> handleStreamFragment(fragment, chunkProcessor), cutterTimeout, requestId);
    }

    public Future<DoubaoResponse> chatStream(
            ChatModel chatModel,
            DoubaoRequest request,
            long cutterTimeout,
            String requestId
    ) {
        request.stream(true);
        DoubaoResponseBuffer buffer = new DoubaoResponseBuffer();
        return chatStream(
                chatModel,
                request,
                chunk -> {
                    buffer.accept(chunk);
                    return Future.succeededFuture();
                },
                cutterTimeout,
                requestId
        )
                .compose(v -> {
                    var x = buffer.build();
                    return Future.succeededFuture(x);
                });
    }
}
