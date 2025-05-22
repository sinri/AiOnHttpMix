package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.request.input;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.0
 */
class QwenRequestInputImpl extends JsonifiableEntityImpl<QwenRequestInput> implements QwenRequestInput {
    public QwenRequestInputImpl() {
        this(new JsonObject());
    }

    public QwenRequestInputImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public @Nonnull QwenRequestInput getImplementation() {
        return this;
    }
}
