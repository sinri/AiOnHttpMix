package io.github.sinri.AiOnHttpMix.deepseek;

import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class DeepseekClient {
    public Future<JsonObject> chat(
            DeepseekServiceMeta serviceMeta,
            JsonObject requestBody,
            String requestId
    ) {
        return serviceMeta.request("https://api.deepseek.com/chat/completions", requestBody, requestId);
    }

    public Future<DeepseekChatResponse> chat(
            DeepseekServiceMeta serviceMeta,
            DeepseekChatRequest request,
            String requestId
    ) {
        return serviceMeta.request("https://api.deepseek.com/chat/completions", request.toJsonObject(), requestId)
                .compose(resp -> {
                    return Future.succeededFuture(DeepseekChatResponse.wrap(resp));
                });
    }

//    public Future<JsonObject> chatSSE(){
//
//    }
}
