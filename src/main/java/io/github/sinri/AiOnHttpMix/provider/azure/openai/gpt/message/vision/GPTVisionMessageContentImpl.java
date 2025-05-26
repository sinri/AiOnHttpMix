package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class GPTVisionMessageContentImpl extends JsonifiableEntityImpl<GPTVisionMessageContent> implements GPTVisionMessageContent {
    public GPTVisionMessageContentImpl() {
        super();
    }

    public GPTVisionMessageContentImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public GPTVisionMessageContent getImplementation() {
        return this;
    }
}
