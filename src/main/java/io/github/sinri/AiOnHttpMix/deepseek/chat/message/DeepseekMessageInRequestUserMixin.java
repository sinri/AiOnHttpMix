package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

/**
 * for system and user
 */
public interface DeepseekMessageInRequestUserMixin<T> extends DeepseekMessageBase<T> {
    /**
     * @param name 可以选填的参与者的名称，为模型提供信息以区分相同角色的参与者。
     */
    default T setName(String name) {
        toJsonObject().put("name", name);
        return getImplementation();
    }
}
