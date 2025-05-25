package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessage;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInRequest;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessageInChatRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessage;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInChatRequest;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

class MixChatMessageImpl extends JsonifiableEntityImpl<MixChatMessage> implements MixChatMessage {
    public MixChatMessageImpl() {
        super();
    }

    public MixChatMessageImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public MixChatMessage setRole(String role) {
        return write("role", role);
    }

    @Override
    public String getRole() {
        return readString("role");
    }

    @Override
    public MixChatMessage setContent(String content) {
        return write("content", content);
    }

    @Override
    public String getContent() {
        return readString("content");
    }

    @Override
    public MixChatMessage setReasoningContent(String reasoningContent) {
        return write("reasoning_content", reasoningContent);
    }

    @Override
    public String getReasoningContent() {
        return readString("reasoning_content");
    }

    @Override
    public MixChatMessage setToolCalls(List<ToolCall> toolCalls) {
        return write("tool_calls", new JsonArray(toolCalls
                .stream()
                .map(ToolCall::toJsonObject)
                .collect(Collectors.toList())));
    }

    @Override
    public List<ToolCall> getToolCalls() {
        var a = readJsonObjectArray("tool_calls");
        if (a == null) {
            return List.of();
        } else {
            return a.stream().map(CommonToolCall::new).collect(Collectors.toList());
        }
    }

    @Override
    public String getToolCallId() {
        return readString("tool_call_id");
    }

    private JsonObject toCommonChatRequestJsonObject() {
        JsonObject j = new JsonObject();
        j.put("role", this.getRole());
        j.put("content", this.getContent());
        var reasoningContent = this.getReasoningContent();
        if (reasoningContent != null) {
            j.put("reasoning_content", reasoningContent);
        }
        List<ToolCall> toolCalls = this.getToolCalls();
        if (!toolCalls.isEmpty()) {
            JsonArray array = new JsonArray();
            toolCalls.forEach(toolCall -> array.add(new JsonObject()
                    .put("id", toolCall.getId())
                    .put("type", toolCall.getType())
                    .put("function",
                            toolCall.getFunction() == null ? null
                                    : new JsonObject()
                                    .put("name", toolCall.getFunction().getName())
                                    .put("arguments", toolCall.getFunction().getArguments()))
                    .put("index", toolCall.getIndex())));
            j.put("tool_calls", array);
        }
        String toolCallId = this.getToolCallId();
        if (toolCallId != null) {
            j.put("tool_call_id", toolCallId);
        }
        return j;
    }

    @Override
    public GPTMessage toGPTMessageInChatRequest() {
        var j = toCommonChatRequestJsonObject();
        return GPTMessageInRequest.wrap(j);
    }

    @Override
    public QwenMessage toQwenMessageInChatRequest() {
        var j = toCommonChatRequestJsonObject();
        return QwenMessageInChatRequest.wrap(j);
    }

    @Override
    public DoubaoMessage toDoubaoMessageInChatRequest() {
        var j = toCommonChatRequestJsonObject();
        return DoubaoMessageInChatRequest.wrap(j);
    }

    @Nonnull
    @Override
    public MixChatMessage getImplementation() {
        return this;
    }

    @Override
    public MixChatMessage setToolCallId(String toolCallId) {
        return write("tool_call_id", toolCallId);
    }
}
