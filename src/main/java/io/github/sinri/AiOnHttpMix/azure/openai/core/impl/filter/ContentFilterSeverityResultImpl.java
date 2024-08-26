package io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterSeverityResult;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class ContentFilterSeverityResultImpl extends UnmodifiableJsonifiableEntityImpl implements ContentFilterSeverityResult {
    public ContentFilterSeverityResultImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
