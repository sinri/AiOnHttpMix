package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

final class ContentFilterSeverityResultImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIContentFilterSeverityResult {
    public ContentFilterSeverityResultImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
