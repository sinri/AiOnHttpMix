package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.request;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatRequestMixin;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class InputImpl implements QwenChatRequestMixin.Input {
    private JsonObject jsonObject;

    public InputImpl() {
        this.jsonObject = new JsonObject();
    }
    public InputImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public QwenChatRequestMixin.Input addMessage(QwenKit.Message message) {
        JsonArray messages = jsonObject.getJsonArray("messages");
        if (messages == null) {
            messages = new JsonArray();
            jsonObject.put("messages", messages);
        }
        messages.add(message.toJsonObject());
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenChatRequestMixin.Input reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
