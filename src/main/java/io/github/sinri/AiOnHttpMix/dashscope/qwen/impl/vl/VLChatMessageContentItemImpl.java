package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.vl.QwenVLChatMessageContentItem;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class VLChatMessageContentItemImpl implements QwenVLChatMessageContentItem {
    private JsonObject jsonObject;

    public VLChatMessageContentItemImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public VLChatMessageContentItemImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenVLChatMessageContentItem reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
