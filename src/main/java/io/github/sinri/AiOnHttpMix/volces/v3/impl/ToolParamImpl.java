package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ToolParamImpl implements VolcesKit.ToolParam {
    private JsonObject jsonObject;

    public ToolParamImpl() {
        this.jsonObject = new JsonObject();
    }

    public ToolParamImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull VolcesKit.ToolParam getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesKit.ToolParam reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
