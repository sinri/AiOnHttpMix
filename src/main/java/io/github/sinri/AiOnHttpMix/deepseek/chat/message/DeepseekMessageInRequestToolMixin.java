package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

public interface DeepseekMessageInRequestToolMixin<T> extends DeepseekMessageBase<T> {
    /**
     * @param tool_call_id 此消息所响应的 tool call 的 ID。
     */
    default T setToolCallId(String tool_call_id) {
        toJsonObject().put("tool_call_id", tool_call_id);
        return getImplementation();
    }
}
