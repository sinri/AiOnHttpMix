package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync.QwenResponse;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class QwenKit {
    public QwenKit() {

    }

    public Future<JsonObject> chat(
            QwenServiceAdapter qwenServiceAdapter,
            ChatModel chatModel,
            JsonObject rawRequest,
            String requestId
    ) {
        return qwenServiceAdapter.request(chatModel, rawRequest, requestId);
    }

    public Future<QwenResponse> chat(
            QwenServiceAdapter qwenServiceAdapter,
            ChatModel chatModel,
            QwenRequest request,
            String requestId
    ) {
        return qwenServiceAdapter.request(chatModel, request.toJsonObject(), requestId)
                                 .compose(rawResponse -> {
                                     return Future.succeededFuture(QwenResponse.wrap(rawResponse));
                                 });
    }

}
