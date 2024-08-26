package io.github.sinri.AiOnHttpMix.moonshot.kimi.impl;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.KimiKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class KimiToolDefinitionImpl implements KimiKit.ToolDefinition {
    private JsonObject jsonObject;

    public KimiToolDefinitionImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public KimiToolDefinitionImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull KimiKit.ToolDefinition getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull KimiKit.ToolDefinition reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
