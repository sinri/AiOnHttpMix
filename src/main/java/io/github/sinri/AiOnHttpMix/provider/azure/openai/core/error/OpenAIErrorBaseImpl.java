package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.error;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

final class OpenAIErrorBaseImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIErrorBase {
    public OpenAIErrorBaseImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
