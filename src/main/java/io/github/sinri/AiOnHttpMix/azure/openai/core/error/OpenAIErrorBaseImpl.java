package io.github.sinri.AiOnHttpMix.azure.openai.core.error;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

final class OpenAIErrorBaseImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIErrorBase {
    public OpenAIErrorBaseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
