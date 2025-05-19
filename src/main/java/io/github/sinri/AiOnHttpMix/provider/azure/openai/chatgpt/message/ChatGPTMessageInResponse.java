package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.message;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.tool.ChatGPTToolCall;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface ChatGPTMessageInResponse extends ChatGPTMessage {
    static ChatGPTMessageInResponse wrap(JsonObject jsonObject) {
        return new ChatGPTMessageImpl(jsonObject);
    }

    // annotations: an array but unknown

    default String getContent() {
        return readString("content");
    }

    default String getRefusal() {
        return readString("refusal");
    }

    default String getRole() {
        return readString("role");
    }

    default List<ChatGPTToolCall> getToolCalls() {
        List<JsonObject> a = readJsonObjectArray("tool_calls");
        if (a == null) return List.of();
        return a.stream().map(ChatGPTToolCall::new).toList();
    }
}
