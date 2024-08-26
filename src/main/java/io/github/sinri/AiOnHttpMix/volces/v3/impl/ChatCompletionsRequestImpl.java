package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChatCompletionsRequestImpl implements VolcesKit.ChatCompletionsRequest {
    private JsonObject jsonObject;

    public ChatCompletionsRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public ChatCompletionsRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull VolcesKit.ChatCompletionsRequest getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull VolcesKit.ChatCompletionsRequest reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
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
