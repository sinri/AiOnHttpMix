package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message;

import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface GPTMessageInResponse extends GPTMessage {
    static GPTMessageInResponse wrap(JsonObject jsonObject) {
        return new GPTMessageImpl(jsonObject);
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

    default List<CommonToolCall> getToolCalls() {
        List<JsonObject> a = readJsonObjectArray("tool_calls");
        if (a == null) return List.of();
        return a.stream().map(CommonToolCall::new).toList();
    }
}
