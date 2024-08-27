package io.github.sinri.AiOnHttpMix.azure.openai.core.filter;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

final class ContentFilterDetectedResultImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIContentFilterDetectedResult {
    public ContentFilterDetectedResultImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
