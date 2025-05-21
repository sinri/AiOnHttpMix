package io.github.sinri.AiOnHttpMix.provider.volces;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseBuffer;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseChunk;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream.DoubaoResponseFragment;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponse;
import io.github.sinri.AiOnHttpMix.utils.models.volces.VolcesModelSeries;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

public class VolcesKit {
    public VolcesKit() {

    }

    public Future<JsonObject> chat(
            VolcesServiceAdapter serviceAdapter,
            VolcesModelSeries chatModel,
            JsonObject rawRequest,
            String requestId
    ) {
        return serviceAdapter.request(chatModel, rawRequest, requestId);
    }

    public Future<DoubaoResponse> chat(
            VolcesServiceAdapter serviceAdapter,
            VolcesModelSeries chatModel,
            DoubaoRequest request,
            String requestId
    ) {
        return serviceAdapter.request(chatModel, request.toJsonObject(), requestId)
                             .compose(rawResponse -> {
                                 return Future.succeededFuture(DoubaoResponse.wrap(rawResponse));
                             });
    }

    public Future<Void> chatStream(
            VolcesServiceAdapter serviceAdapter,
            VolcesModelSeries chatModel,
            JsonObject rawRequest,
            Function<String, Future<Void>> fragmentProcessor,
            long cutterTimeout,
            String requestId
    ) {
        rawRequest.put("stream", true);
        return serviceAdapter.requestStream(chatModel, rawRequest, fragmentProcessor, cutterTimeout, requestId);
    }

    public Future<Void> chatStream(
            VolcesServiceAdapter serviceAdapter,
            VolcesModelSeries chatModel,
            DoubaoRequest request,
            Function<DoubaoResponseChunk, Future<Void>> chunkProcessor,
            long cutterTimeout,
            String requestId
    ) {
        request.stream(true);
        return serviceAdapter.requestStream(chatModel, request.toJsonObject(), fragment -> {
            try {
                DoubaoResponseFragment f = DoubaoResponseFragment.wrap(fragment);
                JsonObject data = f.getData();
                if (data != null) {
                    DoubaoResponseChunk c = DoubaoResponseChunk.wrap(data);
                    return chunkProcessor.apply(c);
                } else {
                    AigcMix.getVerboseLogger().warning("in fragment data is null");
                    return Future.succeededFuture();
                }
            } catch (Throwable throwable) {
                AigcMix.getVerboseLogger().exception(throwable);
                return Future.failedFuture(throwable);
            }
        }, cutterTimeout, requestId);
    }

    public Future<DoubaoResponse> chatStream(
            VolcesServiceAdapter serviceAdapter,
            VolcesModelSeries chatModel,
            DoubaoRequest request,
            long cutterTimeout,
            String requestId
    ) {
        request.stream(true);
        DoubaoResponseBuffer buffer = new DoubaoResponseBuffer();
        return serviceAdapter.requestStream(chatModel, request.toJsonObject(), fragment -> {
                                 try {
                                     DoubaoResponseFragment f = DoubaoResponseFragment.wrap(fragment);
                                     JsonObject data = f.getData();
                                     if (data != null) {
                                         DoubaoResponseChunk c = DoubaoResponseChunk.wrap(data);
                                         buffer.accept(c);
                                     } else {
                                         AigcMix.getVerboseLogger().warning("in fragment data is null");
                                     }
                                     return Future.succeededFuture();
                                 } catch (Throwable throwable) {
                                     AigcMix.getVerboseLogger().exception(throwable);
                                     return Future.failedFuture(throwable);
                                 }
                             }, cutterTimeout, requestId)
                             .compose(v -> {
                                 var x = buffer.build();
                                 return Future.succeededFuture(x);
                             });
    }
}
