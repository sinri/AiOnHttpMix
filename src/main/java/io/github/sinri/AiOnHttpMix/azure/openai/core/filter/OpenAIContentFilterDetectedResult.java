package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface OpenAIContentFilterDetectedResult extends UnmodifiableJsonifiableEntity {
    static OpenAIContentFilterDetectedResult wrap(JsonObject jsonObject) {
        return new ContentFilterDetectedResultImpl(jsonObject);
    }

    default Boolean isFiltered() {
        return readBoolean("filtered");
    }

    default Boolean isDetected() {
        return readBoolean("detected");
    }
}
