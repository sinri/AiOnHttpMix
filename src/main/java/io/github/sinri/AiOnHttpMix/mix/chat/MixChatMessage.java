package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessage;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessage;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface MixChatMessage extends JsonifiableEntity<MixChatMessage> {
    static MixChatMessage create() {
        return new MixChatMessageImpl();
    }

    static MixChatMessage wrap(JsonObject jsonObject) {
        return new MixChatMessageImpl(jsonObject);
    }

    String getRole();

    MixChatMessage setRole(String role);

    String getContent();

    MixChatMessage setContent(String content);

    String getReasoningContent();

    MixChatMessage setReasoningContent(String reasoningContent);

    List<ToolCall> getToolCalls();

    MixChatMessage setToolCalls(List<ToolCall> toolCalls);

    String getToolCallId();

    MixChatMessage setToolCallId(String toolCallId);

    GPTMessage toGPTMessageInChatRequest();

    QwenMessage toQwenMessageInChatRequest();

    DoubaoMessage toDoubaoMessageInChatRequest();
}
