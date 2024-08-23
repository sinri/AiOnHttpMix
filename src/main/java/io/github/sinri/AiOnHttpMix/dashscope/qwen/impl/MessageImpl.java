package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MessageImpl implements QwenKit.Message {
    private JsonObject jsonObject;

    public MessageImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public MessageImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull MessageImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Override
    public @NotNull QwenKit.Message getImplementation() {
        return this;
    }

    @Override
    public @Nullable List<QwenKit.ToolCall> getToolCalls() {
        List<JsonObject> toolCalls = readJsonObjectArray("tool_calls");
        if (toolCalls == null) return null;
        List<QwenKit.ToolCall> list = new ArrayList<>();
        toolCalls.forEach(x -> {
            QwenKit.ToolCall tc = QwenKit.ToolCall.wrap(x);
            list.add(tc);
        });
        return list;
    }
}
