package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message;

import io.github.sinri.keel.core.json.JsonifiableEntity;
/**
 * QwenMessage 接口，定义了 Qwen 系列消息对象的基本结构和 JSON 序列化能力。
 * 提供消息角色的读取与设置方法，便于消息的统一处理。
 * @since 2.0.0
 */
public interface QwenMessage extends JsonifiableEntity<QwenMessage> {

    default String role() {
        return readString("role");
    }

    default QwenMessage role(String role) {
        return write("role", role);
    }
}
