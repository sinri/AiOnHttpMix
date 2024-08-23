package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class VLChatRequestImpl implements QwenKit.VLChatRequest {
    private JsonObject jsonObject;

    public VLChatRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public VLChatRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull QwenKit.VLChatRequest getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenKit.VLChatRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
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
