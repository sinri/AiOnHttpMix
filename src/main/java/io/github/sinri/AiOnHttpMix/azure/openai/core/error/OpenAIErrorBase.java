package io.github.sinri.AiOnHttpMix.azure.openai.core.error;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface OpenAIErrorBase extends UnmodifiableJsonifiableEntity {
    static OpenAIErrorBase wrap(JsonObject json) {
        return new OpenAIErrorBaseImpl(json);
    }

    default String getCode() {
        return readString("code");
    }

    default String getMessage() {
        return readString("message");
    }
}
