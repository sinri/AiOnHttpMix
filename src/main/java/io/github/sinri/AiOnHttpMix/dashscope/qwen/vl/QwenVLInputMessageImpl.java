package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenVLInputMessageImpl implements QwenVLInputMessage {
    private JsonObject jsonObject;

    public QwenVLInputMessageImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public QwenVLInputMessageImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenVLInputMessage reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

}
