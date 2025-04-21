package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.3.1
 */
class QwenRequestParametersTranslationOptionsImpl implements QwenRequest.Parameters.TranslationOptions {
    private JsonObject jsonObject;

    public QwenRequestParametersTranslationOptionsImpl() {
        this(new JsonObject());
    }

    public QwenRequestParametersTranslationOptionsImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }


    @Override
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public @NotNull QwenRequest.Parameters.TranslationOptions reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
