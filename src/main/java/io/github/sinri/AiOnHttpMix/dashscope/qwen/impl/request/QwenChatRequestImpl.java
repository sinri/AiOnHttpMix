package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.request;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class QwenChatRequestImpl implements QwenKit.ChatRequest {
    private JsonObject jsonObject;

    public QwenChatRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public QwenChatRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenChatRequestImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Override
    public @NotNull QwenChatRequestImpl getImplementation() {
        return this;
    }
}
