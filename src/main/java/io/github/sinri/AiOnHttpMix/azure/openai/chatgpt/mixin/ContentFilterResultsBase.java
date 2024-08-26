package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterDetectedResult;
import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterSeverityResult;
import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.error.OpenAIErrorMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import org.jetbrains.annotations.Nullable;

public interface ContentFilterResultsBase extends UnmodifiableJsonifiableEntity {
    @Nullable
    ContentFilterSeverityResult getSexual();

    @Nullable
    ContentFilterSeverityResult getViolence();

    @Nullable
    ContentFilterSeverityResult getHate();

    @Nullable
    ContentFilterSeverityResult getSelfHarm();

    @Nullable
    ContentFilterDetectedResult getProfanity();

    @Nullable
    OpenAIErrorMixin.ErrorBase getError();
}
