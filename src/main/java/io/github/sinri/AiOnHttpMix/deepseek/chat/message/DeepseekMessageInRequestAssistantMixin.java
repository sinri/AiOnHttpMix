package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

/**
 * For assistant in request
 */
public interface DeepseekMessageInRequestAssistantMixin<T> extends DeepseekMessageBase<T> {
    /**
     * (Beta)
     * 设置此参数为 true，来强制模型在其回答中以此 assistant 消息中提供的前缀内容开始。
     * 您必须设置 base_url="https://api.deepseek.com/beta" 来使用此功能。
     */
    default T setAssistantPrefix(boolean prefix) {
        toJsonObject().put("prefix", prefix);
        return getImplementation();
    }

    /**
     * (Beta)
     * 用于 deepseek-reasoner 模型在对话前缀续写功能下，作为最后一条 assistant 思维链内容的输入。
     * 使用此功能时，prefix 参数必须设置为 true。
     */
    default T setAssistantReasoningContent(String reasoning_content) {
        toJsonObject().put("reasoning_content", reasoning_content);
        return getImplementation();
    }
}
