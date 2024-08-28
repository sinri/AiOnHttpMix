package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class VolcesChatMessageToolCallForRequestImpl implements VolcesChatMessageToolCallForRequest {
    private JsonObject jsonObject;

    public VolcesChatMessageToolCallForRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public VolcesChatMessageToolCallForRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesChatMessageToolCallForRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
