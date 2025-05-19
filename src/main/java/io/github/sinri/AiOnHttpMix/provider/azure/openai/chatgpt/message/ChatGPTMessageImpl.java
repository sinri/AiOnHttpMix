package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.message;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ChatGPTMessageImpl extends JsonifiableEntityImpl<ChatGPTMessage> implements ChatGPTMessageInRequest, ChatGPTMessageInResponse {
    public ChatGPTMessageImpl() {
        super();
    }

    public ChatGPTMessageImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public ChatGPTMessage getImplementation() {
        return this;
    }
}
