package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.response;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAICreateChatCompletionResponseMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAICreateChatCompletionResponseMixin.Choice {
    public ChoiceImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
