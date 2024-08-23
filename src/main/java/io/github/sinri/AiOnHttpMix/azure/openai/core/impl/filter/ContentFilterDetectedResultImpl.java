package io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter;

import io.github.sinri.AiOnHttpMix.azure.openai.core.ContentFilterDetectedResult;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class ContentFilterDetectedResultImpl extends UnmodifiableJsonifiableEntityImpl implements ContentFilterDetectedResult {
    public ContentFilterDetectedResultImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
