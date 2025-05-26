package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;

public interface OpenAIContentFilterPromptResults extends OpenAIContentFilterResultsBase {
    static OpenAIContentFilterPromptResults wrap(JsonObject jsonObject) {
        return new ContentFilterPromptResultsImpl(jsonObject);
    }

    @Nullable
    OpenAIContentFilterDetectedResult getJailbreak();

}
