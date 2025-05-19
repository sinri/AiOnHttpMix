package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.request.ChatGPTRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.response.sync.ChatGPTResponse;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class ChatGPTKit {
    public Future<JsonObject> chat(
            ChatGPTServiceAdapter serviceAdapter,
            ChatModel chatModel,
            JsonObject rawRequest,
            String requestId
    ) {
        return serviceAdapter.request(
                chatModel,
                rawRequest,
                requestId
        );
    }

    public Future<ChatGPTResponse> chat(
            ChatGPTServiceAdapter serviceAdapter,
            ChatModel chatModel,
            ChatGPTRequest request,
            String requestId
    ) {
        return serviceAdapter.request(
                                     chatModel,
                                     request.toJsonObject(),
                                     requestId
                             )
                             .compose(jsonObject -> {
                                 return Future.succeededFuture(ChatGPTResponse.wrap(jsonObject));
                             });
    }
}
