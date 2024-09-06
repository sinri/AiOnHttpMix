package io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3;

import io.github.sinri.AiOnHttpMix.azure.openai.core.filter.OpenAIContentFilterSeverityResult;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface DalleContentFilterResults extends UnmodifiableJsonifiableEntity {
    static DalleContentFilterResults wrap(JsonObject j) {
        return new DalleContentFilterResultsImpl(j);
    }

    @Nullable
    default OpenAIContentFilterSeverityResult getSexual() {
        JsonObject entries = readJsonObject("sexual");
        if (entries == null) return null;
        return OpenAIContentFilterSeverityResult.wrap(entries);
    }

    @Nullable
    default OpenAIContentFilterSeverityResult getViolence() {
        JsonObject entries = readJsonObject("violence");
        if (entries == null) return null;
        return OpenAIContentFilterSeverityResult.wrap(entries);
    }

    @Nullable
    default OpenAIContentFilterSeverityResult getHate() {
        JsonObject entries = readJsonObject("hate");
        if (entries == null) return null;
        return OpenAIContentFilterSeverityResult.wrap(entries);
    }

    @Nullable
    default OpenAIContentFilterSeverityResult getSelfHarm() {
        JsonObject entries = readJsonObject("self_harm");
        if (entries == null) return null;
        return OpenAIContentFilterSeverityResult.wrap(entries);
    }
}
