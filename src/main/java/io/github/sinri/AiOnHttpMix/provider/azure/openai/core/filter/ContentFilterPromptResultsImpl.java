package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

final class ContentFilterPromptResultsImpl extends ContentFilterResultsBaseImpl implements OpenAIContentFilterPromptResults {
    public ContentFilterPromptResultsImpl(@Nonnull JsonObject jsonObject) {
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
