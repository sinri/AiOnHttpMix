package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface OpenAIContentFilterPromptResults extends OpenAIContentFilterResultsBase {
    static OpenAIContentFilterPromptResults wrap(JsonObject jsonObject) {
        return new ContentFilterPromptResultsImpl(jsonObject);
    }

    @Nullable
    OpenAIContentFilterDetectedResult getJailbreak();

}
