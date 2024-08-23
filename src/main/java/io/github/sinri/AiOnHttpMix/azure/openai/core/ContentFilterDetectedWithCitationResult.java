package io.github.sinri.AiOnHttpMix.azure.openai.core;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface ContentFilterDetectedWithCitationResult extends UnmodifiableJsonifiableEntity {
    @Nullable
    default JsonObject getCitation() {
        // URL as string,
        // license as string
        return readJsonObject("citation");
    }
}
