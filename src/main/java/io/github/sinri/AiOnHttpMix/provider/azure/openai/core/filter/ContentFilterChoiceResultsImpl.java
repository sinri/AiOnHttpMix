package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

final class ContentFilterChoiceResultsImpl extends ContentFilterResultsBaseImpl implements OpenAIContentFilterChoiceResults {
    public ContentFilterChoiceResultsImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
