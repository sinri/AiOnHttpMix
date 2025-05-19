package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ChatGPTResponseChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements ChatGPTResponseChoice {
    public ChatGPTResponseChoiceImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
