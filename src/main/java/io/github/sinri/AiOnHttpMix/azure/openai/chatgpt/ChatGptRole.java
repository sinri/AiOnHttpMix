package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt;

/**
 * <a href="https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#chatcompletionrequestmessagerole">chatCompletionRequestMessageRole</a>
 */
public enum ChatGptRole {
    system,
    user,
    assistant,
    @Deprecated function,
    tool
}
