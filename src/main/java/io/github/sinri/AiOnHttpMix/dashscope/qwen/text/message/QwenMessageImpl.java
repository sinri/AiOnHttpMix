package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolCall;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

class QwenMessageImpl implements QwenMessage {
    private JsonObject jsonObject;

    public QwenMessageImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public QwenMessageImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull QwenMessageImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }


    @Override
    public @Nullable List<QwenToolCall> getToolCalls() {
        List<JsonObject> toolCalls = readJsonObjectArray("tool_calls");
        if (toolCalls == null) return null;
        List<QwenToolCall> list = new ArrayList<>();
        toolCalls.forEach(x -> {
            QwenToolCall tc = QwenToolCall.wrap(x);
            list.add(tc);
        });
        return list;
    }
}
