package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenVLMessageContentItemImpl implements QwenVLMessageContentItem {
    private JsonObject jsonObject;

    public QwenVLMessageContentItemImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public QwenVLMessageContentItemImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenVLMessageContentItem reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
