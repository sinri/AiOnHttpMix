package io.github.sinri.AiOnHttpMix.volces.v3.tool;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class VolcesChatFunctionDefinitionImpl implements VolcesChatFunctionDefinition {
    private JsonObject jsonObject;

    public VolcesChatFunctionDefinitionImpl() {
        this.jsonObject = new JsonObject();
    }

    public VolcesChatFunctionDefinitionImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull VolcesChatFunctionDefinition getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesChatFunctionDefinition reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

}
