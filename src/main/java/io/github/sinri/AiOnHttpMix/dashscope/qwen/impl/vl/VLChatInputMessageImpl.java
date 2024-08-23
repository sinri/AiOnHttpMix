package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class VLChatInputMessageImpl implements QwenKit.VLChatInputMessage {
    private JsonObject jsonObject;

    public VLChatInputMessageImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public VLChatInputMessageImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull QwenKit.VLChatInputMessage getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenKit.VLChatInputMessage reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

}
