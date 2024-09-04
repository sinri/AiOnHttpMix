package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenRequestParametersSearchOptionsImpl implements QwenRequest.Parameters.SearchOptions {
    private JsonObject jsonObject;

    public QwenRequestParametersSearchOptionsImpl() {
        this.jsonObject = new JsonObject();
    }

    public QwenRequestParametersSearchOptionsImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public @NotNull QwenRequest.Parameters.SearchOptions reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
