package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;

public enum QwenVLRole {
    system, user, assistant;

    /**
     * @since 1.3.0
     */
    public AnyLLMRole toAnyLLMRole() {
        return switch (this) {
            case system -> AnyLLMRole.system;
            case user -> AnyLLMRole.user;
            case assistant -> AnyLLMRole.assistant;
        };
    }
}
