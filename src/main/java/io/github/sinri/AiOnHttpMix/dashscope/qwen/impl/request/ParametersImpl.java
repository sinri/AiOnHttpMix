package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.request;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatRequestMixin;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ParametersImpl implements QwenChatRequestMixin.Parameters {
    private JsonObject jsonObject;

    public ParametersImpl() {
        this.jsonObject = new JsonObject();
    }

    public ParametersImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
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
