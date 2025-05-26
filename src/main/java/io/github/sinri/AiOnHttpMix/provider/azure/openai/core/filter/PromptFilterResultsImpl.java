package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import java.util.Objects;

final class PromptFilterResultsImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIPromptFilterResults {
    public PromptFilterResultsImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public OpenAIContentFilterPromptResults getContentFilterResults() {
        JsonObject contentFilterResults = readJsonObject("content_filter_results");
        return new ContentFilterPromptResultsImpl(Objects.requireNonNull(contentFilterResults));
    }
}
