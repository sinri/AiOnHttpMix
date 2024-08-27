package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.core.error.OpenAIErrorBase;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ContentFilterResultsBaseImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIContentFilterResultsBase {
    public ContentFilterResultsBaseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    @Nullable
    public OpenAIContentFilterSeverityResult getSexual() {
        JsonObject x = readJsonObject("sexual");
        if (x == null) return null;
        return new ContentFilterSeverityResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public OpenAIContentFilterSeverityResult getViolence() {
        JsonObject x = readJsonObject("violence");
        if (x == null) return null;
        return new ContentFilterSeverityResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public OpenAIContentFilterSeverityResult getHate() {
        JsonObject x = readJsonObject("hate");
        if (x == null) return null;
        return new ContentFilterSeverityResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public OpenAIContentFilterSeverityResult getSelfHarm() {
        JsonObject x = readJsonObject("self_harm");
        if (x == null) return null;
        return new ContentFilterSeverityResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public OpenAIContentFilterDetectedResult getProfanity() {
        JsonObject x = readJsonObject("profanity");
        if (x == null) return null;
        return new ContentFilterDetectedResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public OpenAIErrorBase getError() {
        JsonObject error = readJsonObject("error");
        if (error != null) {
            return OpenAIErrorBase.wrap(error);
        }
        return null;
    }
}
