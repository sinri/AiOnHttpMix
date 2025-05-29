package io.github.sinri.AiOnHttpMix.mix.chat.response.stream;

import java.util.List;

import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface MixChatResponseChunkChoice extends JsonifiableEntity<MixChatResponseChunkChoice> {
    static MixChatResponseChunkChoice create() {
        return new MixChatResponseChunkChoiceImpl();
    }

    static MixChatResponseChunkChoice wrap(JsonObject x) {
        return new MixChatResponseChunkChoiceImpl(x);
    }

    default String getRole() {
        return readString("role");
    }

    default MixChatResponseChunkChoice setRole(String role) {
        return write("role", role);
    }

    default String getContent() {
        return readString("content");
    }

    default MixChatResponseChunkChoice setContent(String content) {
        return write("content", content);
    }

    default String getReasoningContent() {
        return readString("reasoning_content");
    }

    default MixChatResponseChunkChoice setReasoningContent(String reasoningContent) {
        return write("reasoning_content", reasoningContent);
    }

    default String getFinishReason() {
        return readString("finish_reason");
    }

    default MixChatResponseChunkChoice setFinishReason(String finishReason) {
        return write("finish_reason", finishReason);
    }

    default MixChatResponseChunkChoice setToolCalls(List<ToolCall> toolCalls) {
        return write("tool_calls", new JsonArray(toolCalls.stream().map(ToolCall::toJsonObject).toList()));
    }

    default List<ToolCall> getToolCalls() {
        var array = readJsonObjectArray("tool_calls");
        if (array == null)
            return List.of();
        return array.stream()
                    .map(CommonToolCall::new)
                .map(x -> (ToolCall) x)
                .toList();
    }

    default Integer getIndex() {
        return readInteger("index");
    }

    default MixChatResponseChunkChoice setIndex(Integer index) {
        return write("index", index);
    }
}
