package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface OpenAIContentFilterDetectedWithCitationResult extends UnmodifiableJsonifiableEntity {
    static OpenAIContentFilterDetectedWithCitationResult wrap(JsonObject jsonObject) {
        return new ContentFilterDetectedWithCitationResultImpl(jsonObject);
    }

    @Nullable
    default JsonObject getCitation() {
        // URL as string,
        // license as string
        return readJsonObject("citation");
    }
}
