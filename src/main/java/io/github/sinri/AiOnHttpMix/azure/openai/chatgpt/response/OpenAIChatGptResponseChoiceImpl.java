package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class OpenAIChatGptResponseChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIChatGptResponseChoice {
    public OpenAIChatGptResponseChoiceImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
