package io.github.sinri.AiOnHttpMix.deepseek.chat;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class DeepseekChatRequestImpl implements DeepseekChatRequest {
    private JsonObject jsonObject;

    public DeepseekChatRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public DeepseekChatRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }


    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull DeepseekChatRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
