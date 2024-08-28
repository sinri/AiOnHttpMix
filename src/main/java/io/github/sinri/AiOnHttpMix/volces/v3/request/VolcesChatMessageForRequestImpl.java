package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class VolcesChatMessageForRequestImpl implements VolcesChatMessageForRequest {
    private JsonObject jsonObject;

    public VolcesChatMessageForRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public VolcesChatMessageForRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesChatMessageForRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
