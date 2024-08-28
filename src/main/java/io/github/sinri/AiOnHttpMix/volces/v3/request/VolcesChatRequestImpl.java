package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class VolcesChatRequestImpl implements VolcesChatRequest {
    private JsonObject jsonObject;

    public VolcesChatRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public VolcesChatRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesChatRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public static class StreamOptionsParamImpl implements StreamOptionsParam {
        private JsonObject jsonObject;

        public StreamOptionsParamImpl() {
            this.jsonObject = new JsonObject();
        }

        public StreamOptionsParamImpl(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public @NotNull JsonObject toJsonObject() {
            return jsonObject;
        }

        @Override
        public @NotNull StreamOptionsParam reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
            this.jsonObject = jsonObject;
            return this;
        }
    }
}
