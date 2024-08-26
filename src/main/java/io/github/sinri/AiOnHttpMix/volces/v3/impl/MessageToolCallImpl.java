package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class MessageToolCallImpl implements VolcesKit.MessageToolCall {
    private JsonObject jsonObject;

    public MessageToolCallImpl() {
        this.jsonObject = new JsonObject();
    }

    public MessageToolCallImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull VolcesKit.MessageToolCall getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesKit.MessageToolCall reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
