package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.core.error.OpenAIErrorBase;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import org.jetbrains.annotations.Nullable;

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
