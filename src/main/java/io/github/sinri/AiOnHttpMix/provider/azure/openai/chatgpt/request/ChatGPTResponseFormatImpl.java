package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ChatGPTResponseFormatImpl extends JsonifiableEntityImpl<ChatGPTResponseFormat> implements ChatGPTResponseFormat {
    public ChatGPTResponseFormatImpl() {
        super();
    }

    public ChatGPTResponseFormatImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public ChatGPTResponseFormat getImplementation() {
        return this;
    }
}
