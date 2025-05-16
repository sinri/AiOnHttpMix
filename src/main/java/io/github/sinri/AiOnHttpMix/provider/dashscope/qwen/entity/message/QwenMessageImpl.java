package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.message;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * QwenMessageImpl 实现了 QwenMessage、QwenMessageInRequest、QwenMessageInResponse 接口。
 * 用于封装 Qwen 消息的数据结构，支持 JSON 序列化与反序列化。
 *
 * @since 2.0.0
 */
class QwenMessageImpl extends JsonifiableEntityImpl<QwenMessage> implements QwenMessageInRequest, QwenMessageInResponse {
    public QwenMessageImpl() {
        super();
    }

    public QwenMessageImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public @Nonnull QwenMessage getImplementation() {
        return this;
    }
}
