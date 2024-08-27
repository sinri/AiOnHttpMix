package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class ContentFilterPromptResultsImpl extends ContentFilterResultsBaseImpl implements OpenAIContentFilterPromptResults {
    public ContentFilterPromptResultsImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }


    @Nullable
    @Override
    public OpenAIContentFilterDetectedResult getJailbreak() {
        JsonObject x = readJsonObject("jailbreak");
        if (x == null) return null;
        return OpenAIContentFilterDetectedResult.wrap(Objects.requireNonNull(x));
    }
}
