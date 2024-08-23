package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.response.ToolCallImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class AssistantMessage implements ChatGPTKit.Message {
    private JsonObject jsonObject;

    public AssistantMessage(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.jsonObject.put("role", ChatCompletionRequestMessageRole.assistant);
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public ChatGPTKit.@NotNull Message reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Nullable
    public String getContent() {
        return readString("content");
    }

    @Nullable
    public List<ChatGPTKit.ToolCall> getToolCalls() {
        var tool_calls = readJsonObjectArray("tool_calls");
        if (tool_calls == null) return null;
        List<ChatGPTKit.ToolCall> list = new ArrayList<>();
        for (var x : tool_calls) {
            list.add(new ToolCallImpl(x));
        }
        return list;
    }
}
