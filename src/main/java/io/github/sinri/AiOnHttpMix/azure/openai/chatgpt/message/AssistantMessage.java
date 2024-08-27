package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class AssistantMessage implements OpenAIChatGptMessage {
    private JsonObject jsonObject;

    public AssistantMessage(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.jsonObject.put("role", ChatGptRole.assistant);
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull OpenAIChatGptMessage reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Nullable
    public String getContent() {
        return readString("content");
    }

    @Nullable
    public List<OpenAIChatGptResponseToolCall> getToolCalls() {
        var tool_calls = readJsonObjectArray("tool_calls");
        if (tool_calls == null) return null;
        List<OpenAIChatGptResponseToolCall> list = new ArrayList<>();
        for (var x : tool_calls) {
            list.add(OpenAIChatGptResponseToolCall.wrap(x));
        }
        return list;
    }
}
