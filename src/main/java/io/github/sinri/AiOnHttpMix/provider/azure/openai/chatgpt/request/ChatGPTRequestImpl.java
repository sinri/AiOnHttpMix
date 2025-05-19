package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ChatGPTRequestImpl extends JsonifiableEntityImpl<ChatGPTRequest> implements ChatGPTRequest {
    public ChatGPTRequestImpl() {
        super();
    }

    public ChatGPTRequestImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public ChatGPTRequest getImplementation() {
        return this;
    }
}
