package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.core.ContentFilterDetectedResult;
import org.jetbrains.annotations.Nullable;

public interface ContentFilterPromptResults extends ContentFilterResultsBase {
    @Nullable
    ContentFilterDetectedResult getJailbreak();

}
