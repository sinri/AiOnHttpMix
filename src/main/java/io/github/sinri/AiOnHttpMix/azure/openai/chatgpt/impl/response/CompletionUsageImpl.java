package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.response;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAICreateChatCompletionResponseMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class CompletionUsageImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAICreateChatCompletionResponseMixin.CompletionUsage {
    public CompletionUsageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
