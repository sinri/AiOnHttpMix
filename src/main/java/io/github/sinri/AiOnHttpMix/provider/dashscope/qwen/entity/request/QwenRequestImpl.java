package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.0
 */
class QwenRequestImpl extends JsonifiableEntityImpl<QwenRequest> implements QwenRequest {

    public QwenRequestImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    public QwenRequestImpl() {
        super();
    }

    @Override
    public @Nonnull QwenRequest getImplementation() {
        return this;
    }
}
