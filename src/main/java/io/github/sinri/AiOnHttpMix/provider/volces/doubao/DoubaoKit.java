package io.github.sinri.AiOnHttpMix.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponse;
import io.github.sinri.AiOnHttpMix.utils.models.DoubaoModelSeries;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class DoubaoKit {
    public DoubaoKit() {

    }

    public Future<JsonObject> chat(
            DoubaoServiceAdapter serviceAdapter,
            DoubaoModelSeries chatModel,
            JsonObject rawRequest,
            String requestId
    ) {
        return serviceAdapter.request(chatModel, rawRequest, requestId);
    }

    public Future<DoubaoResponse> chat(
            DoubaoServiceAdapter serviceAdapter,
            DoubaoModelSeries chatModel,
            DoubaoRequest request,
            String requestId
    ) {
        return serviceAdapter.request(chatModel, request.toJsonObject(), requestId)
                             .compose(rawResponse -> {
                                 return Future.succeededFuture(DoubaoResponse.wrap(rawResponse));
                             });
    }
}
