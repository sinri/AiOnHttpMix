package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekRole;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;

/**
 * @since 1.1.0
 * @since 1.1.12 add DeepSeek
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

    public DeepseekRole toDeepseekRole() {
        return switch (this) {
            case system -> DeepseekRole.system;
            case user -> DeepseekRole.user;
            case assistant -> DeepseekRole.assistant;
            case tool -> DeepseekRole.tool;
        };
    }
}
