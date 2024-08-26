package io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterDetectedWithCitationResult;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class ContentFilterDetectedWithCitationResultImpl extends UnmodifiableJsonifiableEntityImpl implements ContentFilterDetectedWithCitationResult {
    public ContentFilterDetectedWithCitationResultImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
