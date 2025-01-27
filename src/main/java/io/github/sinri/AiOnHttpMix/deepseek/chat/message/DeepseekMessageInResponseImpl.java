package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class DeepseekMessageInResponseImpl implements DeepseekMessageInResponse {
    private JsonObject jsonObject;

    public DeepseekMessageInResponseImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public DeepseekMessageInResponseImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull DeepseekMessageInResponse getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull DeepseekMessageInResponse reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
