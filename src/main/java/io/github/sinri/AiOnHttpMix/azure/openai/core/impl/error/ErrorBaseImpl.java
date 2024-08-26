package io.github.sinri.AiOnHttpMix.azure.openai.core.impl.error;

import io.github.sinri.AiOnHttpMix.azure.openai.core.mixin.error.OpenAIErrorMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ErrorBaseImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIErrorMixin.ErrorBase {
    public ErrorBaseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
