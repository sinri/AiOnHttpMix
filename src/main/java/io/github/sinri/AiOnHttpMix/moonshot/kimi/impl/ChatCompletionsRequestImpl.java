package io.github.sinri.AiOnHttpMix.moonshot.kimi.impl;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.KimiKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChatCompletionsRequestImpl implements KimiKit.ChatCompletionsRequest {
    private JsonObject jsonObject;

    public ChatCompletionsRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }


    public ChatCompletionsRequestImpl() {
        jsonObject = new JsonObject();
    }

    @Override
    public @NotNull KimiKit.ChatCompletionsRequest getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull KimiKit.ChatCompletionsRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
