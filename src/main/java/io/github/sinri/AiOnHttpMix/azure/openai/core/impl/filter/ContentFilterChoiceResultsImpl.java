package io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.ContentFilterChoiceResults;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class ContentFilterChoiceResultsImpl extends ContentFilterResultsBaseImpl implements ContentFilterChoiceResults {
    public ContentFilterChoiceResultsImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
