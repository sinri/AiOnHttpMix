package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;

/**
 * <a href="https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#chatcompletionrequestmessagerole">chatCompletionRequestMessageRole</a>
 */
public enum ChatGptRole {
    system,
    user,
    assistant,
    @Deprecated function,
    tool;

    /**
     * @since 1.1.5
     */
    public AnyLLMRole toAnyLLMRole() {
        return switch (this) {
            case system -> AnyLLMRole.system;
            case user -> AnyLLMRole.user;
            case assistant -> AnyLLMRole.assistant;
            case tool, function -> AnyLLMRole.tool;
        };
    }
}
