package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

final class ContentFilterDetectedWithCitationResultImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIContentFilterDetectedWithCitationResult {
    public ContentFilterDetectedWithCitationResultImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
