package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class VolcesChatToolDefinitionImpl implements VolcesChatToolDefinition {
    private JsonObject jsonObject;

    public VolcesChatToolDefinitionImpl() {
        this.jsonObject = new JsonObject();
    }

    public VolcesChatToolDefinitionImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesChatToolDefinitionImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
