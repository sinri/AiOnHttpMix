package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

final class ContentFilterDetectedResultImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIContentFilterDetectedResult {
    public ContentFilterDetectedResultImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
