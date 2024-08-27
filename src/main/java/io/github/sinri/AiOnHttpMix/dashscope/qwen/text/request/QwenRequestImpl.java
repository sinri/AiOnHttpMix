package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenRequestImpl implements QwenRequest {
    private JsonObject jsonObject;

    public QwenRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public QwenRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenRequestImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
