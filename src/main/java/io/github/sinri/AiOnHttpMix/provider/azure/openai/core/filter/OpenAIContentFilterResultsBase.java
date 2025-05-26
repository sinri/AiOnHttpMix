package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.core.error.OpenAIErrorBase;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

import javax.annotation.Nullable;

public interface OpenAIContentFilterResultsBase extends UnmodifiableJsonifiableEntity {
    @Nullable
    OpenAIContentFilterSeverityResult getSexual();

    @Nullable
    OpenAIContentFilterSeverityResult getViolence();

    @Nullable
    OpenAIContentFilterSeverityResult getHate();

    @Nullable
    OpenAIContentFilterSeverityResult getSelfHarm();

    @Nullable
    OpenAIContentFilterDetectedResult getProfanity();

    @Nullable
    OpenAIErrorBase getError();
}
