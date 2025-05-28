package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class PromptFilterResultsImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIPromptFilterResults {
    public PromptFilterResultsImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    @Nullable
    public OpenAIContentFilterPromptResults getContentFilterResults() {
        JsonObject cfr = readJsonObject("content_filter_results");
        if (cfr == null) return null;
        return new ContentFilterPromptResultsImpl(cfr);
    }
}
