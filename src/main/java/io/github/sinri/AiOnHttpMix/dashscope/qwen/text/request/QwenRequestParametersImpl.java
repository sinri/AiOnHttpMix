package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenRequestParametersImpl implements QwenRequest.Parameters {
    private JsonObject jsonObject;

    public QwenRequestParametersImpl() {
        this.jsonObject = new JsonObject();
    }

    public QwenRequestParametersImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenRequestParametersImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
