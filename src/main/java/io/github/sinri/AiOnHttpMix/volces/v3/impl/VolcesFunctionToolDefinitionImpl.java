package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class VolcesFunctionToolDefinitionImpl implements VolcesKit.FunctionDefinition {
    private JsonObject jsonObject;

    public VolcesFunctionToolDefinitionImpl() {
        this.jsonObject = new JsonObject();
    }

    public VolcesFunctionToolDefinitionImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull VolcesKit.FunctionDefinition getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesKit.FunctionDefinition reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public static class Builder extends FunctionToolDefinition.FunctionToolDefinitionBuilder<Builder, VolcesKit.FunctionDefinition> {

        @Override
        public VolcesKit.FunctionDefinition build() {
            return new VolcesFunctionToolDefinitionImpl(toJsonObject());
        }

        @Override
        public @NotNull Builder getImplementation() {
            return this;
        }
    }
}
