package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class DeepseekMessageInRequestImpl implements DeepseekMessageInRequest {
    private JsonObject jsonObject;

    public DeepseekMessageInRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public DeepseekMessageInRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull DeepseekMessageInRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Override
    public @NotNull DeepseekMessageInRequest getImplementation() {
        return this;
    }
}
