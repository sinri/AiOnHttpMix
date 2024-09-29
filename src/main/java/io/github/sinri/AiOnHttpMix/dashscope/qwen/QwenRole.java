package io.github.sinri.AiOnHttpMix.dashscope.qwen;

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
}
