package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.input;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenRequestInputImpl extends JsonifiableEntityImpl<QwenRequestInput> implements QwenRequestInput {
    public QwenRequestInputImpl() {
        this(new JsonObject());
    }

    public QwenRequestInputImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public @NotNull QwenRequestInput getImplementation() {
        return this;
    }
}
