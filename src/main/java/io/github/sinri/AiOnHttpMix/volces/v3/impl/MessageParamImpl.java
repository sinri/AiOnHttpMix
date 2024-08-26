package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class MessageParamImpl implements VolcesKit.MessageParam {
    private JsonObject jsonObject;

    public MessageParamImpl() {
        this.jsonObject = new JsonObject();
    }

    public MessageParamImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull VolcesKit.MessageParam getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesKit.MessageParam reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
