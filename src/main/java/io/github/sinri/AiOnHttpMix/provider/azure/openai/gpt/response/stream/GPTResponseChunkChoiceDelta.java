package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.tool.GPTToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface GPTResponseChunkChoiceDelta extends UnmodifiableJsonifiableEntity {
    static GPTResponseChunkChoiceDelta wrap(JsonObject jsonObject) {
        return new GPTResponseChunkChoiceDeltaImpl(jsonObject);
    }

    default String getContent() {
        return readString("content");
    }

    default String getRefusal() {
        return readString("refusal");
    }

    default String getRole() {
        return readString("role");
    }

    default List<GPTToolCall> getToolCalls() {
        List<JsonObject> a = readJsonObjectArray("tool_calls");
        if (a == null) return List.of();
        return a.stream().map(GPTToolCall::new).toList();
    }
}
