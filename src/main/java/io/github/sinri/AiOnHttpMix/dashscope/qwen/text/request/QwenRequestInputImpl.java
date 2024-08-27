package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenRequestInputImpl implements QwenRequest.Input {
    private JsonObject jsonObject;

    public QwenRequestInputImpl() {
        this.jsonObject = new JsonObject();
    }

    public QwenRequestInputImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public QwenRequest.Input addMessage(QwenMessage message) {
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
    public @NotNull QwenRequest.Input reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
