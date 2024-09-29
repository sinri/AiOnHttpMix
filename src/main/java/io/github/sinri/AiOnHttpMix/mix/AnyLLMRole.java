package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;

/**
 * @since 1.1.0
 */
public enum AnyLLMRole {
    system,
    user,
    assistant,
    tool;

    /**
     * @return
     * @since 1.1.5
     */
    public ChatGptRole toChatGptRole() {
        return switch (this) {
            case system -> ChatGptRole.system;
            case user -> ChatGptRole.user;
            case assistant -> ChatGptRole.assistant;
            case tool -> ChatGptRole.tool;
        };
    }

    /**
     * @return
     * @since 1.1.5
     */
    public QwenRole toQwenRole() {
        return switch (this) {
            case system -> QwenRole.system;
            case user -> QwenRole.user;
            case assistant -> QwenRole.assistant;
            case tool -> QwenRole.tool;
        };
    }

    /**
     * @return
     * @since 1.1.5
     */
    public VolcesChatRole toVolcesChatRole() {
        return switch (this) {
            case system -> VolcesChatRole.system;
            case user -> VolcesChatRole.user;
            case assistant -> VolcesChatRole.assistant;
            case tool -> VolcesChatRole.tool;
        };
    }
}
