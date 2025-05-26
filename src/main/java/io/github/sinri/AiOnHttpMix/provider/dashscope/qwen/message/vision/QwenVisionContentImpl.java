package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.vision;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class QwenVisionContentImpl extends JsonifiableEntityImpl<QwenVisionContent> implements QwenVisionContent {

    public QwenVisionContentImpl() {
        super();
    }

    public QwenVisionContentImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public QwenVisionContent getImplementation() {
        return this;
    }
}
