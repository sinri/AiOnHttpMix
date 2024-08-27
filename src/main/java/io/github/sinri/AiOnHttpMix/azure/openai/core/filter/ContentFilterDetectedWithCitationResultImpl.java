package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

final class ContentFilterDetectedWithCitationResultImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIContentFilterDetectedWithCitationResult {
    public ContentFilterDetectedWithCitationResultImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
