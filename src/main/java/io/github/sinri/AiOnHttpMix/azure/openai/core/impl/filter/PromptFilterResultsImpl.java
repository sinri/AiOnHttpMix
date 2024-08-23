package io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.ContentFilterPromptResults;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.PromptFilterResults;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class PromptFilterResultsImpl extends UnmodifiableJsonifiableEntityImpl implements PromptFilterResults {
    public PromptFilterResultsImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public ContentFilterPromptResults getContentFilterResults() {
        JsonObject contentFilterResults = readJsonObject("content_filter_results");
        return new ContentFilterPromptResultsImpl(Objects.requireNonNull(contentFilterResults));
    }
}
