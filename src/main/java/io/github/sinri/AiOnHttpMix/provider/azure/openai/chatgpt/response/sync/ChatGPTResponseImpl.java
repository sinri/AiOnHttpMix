package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ChatGPTResponseImpl extends UnmodifiableJsonifiableEntityImpl implements ChatGPTResponse {
    public ChatGPTResponseImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
