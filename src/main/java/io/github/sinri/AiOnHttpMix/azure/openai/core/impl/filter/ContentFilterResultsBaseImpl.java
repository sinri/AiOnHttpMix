package io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.*;
import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.error.ErrorBaseImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterDetectedResult;
import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.filter.ContentFilterSeverityResult;
import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.error.OpenAIErrorMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ContentFilterResultsBaseImpl extends UnmodifiableJsonifiableEntityImpl implements ContentFilterResultsBase {
    public ContentFilterResultsBaseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    @Nullable
    public ContentFilterSeverityResult getSexual() {
        JsonObject x = readJsonObject("sexual");
        if (x == null) return null;
        return new ContentFilterSeverityResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public ContentFilterSeverityResult getViolence() {
        JsonObject x = readJsonObject("violence");
        if (x == null) return null;
        return new ContentFilterSeverityResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public ContentFilterSeverityResult getHate() {
        JsonObject x = readJsonObject("hate");
        if (x == null) return null;
        return new ContentFilterSeverityResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public ContentFilterSeverityResult getSelfHarm() {
        JsonObject x = readJsonObject("self_harm");
        if (x == null) return null;
        return new ContentFilterSeverityResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public ContentFilterDetectedResult getProfanity() {
        JsonObject x = readJsonObject("profanity");
        if (x == null) return null;
        return new ContentFilterDetectedResultImpl(Objects.requireNonNull(x));
    }

    @Nullable
    @Override
    public OpenAIErrorMixin.ErrorBase getError() {
        JsonObject error = readJsonObject("error");
        if (error != null) {
            return new ErrorBaseImpl(error);
        }
        return null;
    }
}
