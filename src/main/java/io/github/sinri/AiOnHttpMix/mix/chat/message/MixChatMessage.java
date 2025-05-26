package io.github.sinri.AiOnHttpMix.mix.chat.message;

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

    String getTextContent();

    MixChatMessage setTextContent(String content);

    String getRole();

    MixChatMessage setRole(String role);

    String getReasoningContent();

    MixChatMessage setReasoningContent(String reasoningContent);

    List<ToolCall> getToolCalls();

    MixChatMessage setToolCalls(List<ToolCall> toolCalls);

    String getToolCallId();

    MixChatMessage setToolCallId(String toolCallId);

    List<MixChatVisionContentElement> getVisionContent();

    MixChatMessage setVisionContent(List<MixChatVisionContentElement> content);

    default boolean isForVision() {
        return !this.getVisionContent().isEmpty();
    }

    default GPTMessage toGPTMessage() {
        if (isForVision()) {
            return toGPTMessageInVisionChatRequest();
        } else {
            return toGPTMessageInTextChatRequest();
        }
    }

    default QwenMessage toQwenMessage() {
        if (isForVision()) {
            return toQwenMessageInVisionChatRequest();
        } else {
            return toQwenMessageInTextChatRequest();
        }
    }

    default DoubaoMessage toDoubaoMessage() {
        if (isForVision()) {
            return toDoubaoMessageInVisionChatRequest();
        } else {
            return toDoubaoMessageInTextChatRequest();
        }
    }

    GPTMessage toGPTMessageInTextChatRequest();

    QwenMessage toQwenMessageInTextChatRequest();

    DoubaoMessage toDoubaoMessageInTextChatRequest();

    GPTMessage toGPTMessageInVisionChatRequest();

    QwenMessage toQwenMessageInVisionChatRequest();

    DoubaoMessage toDoubaoMessageInVisionChatRequest();
}
