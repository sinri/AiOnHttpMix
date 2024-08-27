package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface OpenAIContentFilterSeverityResult extends UnmodifiableJsonifiableEntity {
    static OpenAIContentFilterSeverityResult wrap(JsonObject jsonObject) {
        return new ContentFilterSeverityResultImpl(jsonObject);
    }

    default Boolean isFiltered() {
        return readBoolean("filtered");
    }

    default String getSeverity() {
        return readString("severity");
    }
}
