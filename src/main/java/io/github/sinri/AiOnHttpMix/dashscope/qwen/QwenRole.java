package io.github.sinri.AiOnHttpMix.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.QwenVLRole;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;

public enum QwenRole {
    system, user, assistant, tool;

    /**
     * @since 1.1.5
     */
    public AnyLLMRole toAnyLLMRole() {
        return switch (this) {
            case system -> AnyLLMRole.system;
            case user -> AnyLLMRole.user;
            case assistant -> AnyLLMRole.assistant;
            case tool -> AnyLLMRole.tool;
        };
    }

    /**
     * @since 1.3.0
     */
    public QwenVLRole toQwenVLRole() {
        return QwenVLRole.valueOf(this.name());
    }
}
