package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenVLRequestImpl implements QwenVLRequest {
    private JsonObject jsonObject;

    public QwenVLRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public QwenVLRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenVLRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public static class InputImpl implements Input {
        private JsonObject jsonObject;

        public InputImpl(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public InputImpl() {
            this.jsonObject = new JsonObject();
        }

        @Override
        public @NotNull JsonObject toJsonObject() {
            return jsonObject;
        }

        @Override
        public @NotNull Input reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
            this.jsonObject = jsonObject;
            return this;
        }
    }

    public static class ParametersImpl implements Parameters {
        private JsonObject jsonObject;

        public ParametersImpl(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public ParametersImpl() {
            this.jsonObject = new JsonObject();
        }

        @Override
        public @NotNull JsonObject toJsonObject() {
            return jsonObject;
        }

        @Override
        public @NotNull ParametersImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
            this.jsonObject = jsonObject;
            return this;
        }
    }
}
