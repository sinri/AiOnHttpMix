package io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.core.ContentFilterDetectedResult;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.ContentFilterPromptResults;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ContentFilterPromptResultsImpl extends ContentFilterResultsBaseImpl implements ContentFilterPromptResults {
    public ContentFilterPromptResultsImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }



    @Nullable
    @Override
    public ContentFilterDetectedResult getJailbreak() {
        JsonObject x = readJsonObject("jailbreak");
        if (x == null) return null;
        return new ContentFilterDetectedResultImpl(Objects.requireNonNull(x));
    }
}
