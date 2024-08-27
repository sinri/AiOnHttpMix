package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

final class ContentFilterSeverityResultImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIContentFilterSeverityResult {
    public ContentFilterSeverityResultImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
