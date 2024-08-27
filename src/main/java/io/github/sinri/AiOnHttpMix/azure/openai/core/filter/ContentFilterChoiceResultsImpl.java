package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

final class ContentFilterChoiceResultsImpl extends ContentFilterResultsBaseImpl implements OpenAIContentFilterChoiceResults {
    public ContentFilterChoiceResultsImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
