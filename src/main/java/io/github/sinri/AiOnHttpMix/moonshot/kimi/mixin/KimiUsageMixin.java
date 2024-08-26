package io.github.sinri.AiOnHttpMix.moonshot.kimi.mixin;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.impl.KimiUsageImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface KimiUsageMixin extends UnmodifiableJsonifiableEntity {
    static KimiUsageMixin wrap(JsonObject jsonObject) {
        return new KimiUsageImpl(jsonObject);
    }

    default Integer getPromptTokens() {
        return readInteger("prompt_tokens");
    }

    default Integer getCompletionTokens() {
        return readInteger("completion_tokens");
    }

    default Integer getTotalTokens() {
        return readInteger("total_tokens");
    }
}
