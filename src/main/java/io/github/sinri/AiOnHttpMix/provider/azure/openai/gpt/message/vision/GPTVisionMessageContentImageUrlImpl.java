package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class GPTVisionMessageContentImageUrlImpl extends JsonifiableEntityImpl<GPTVisionMessageContentImageUrl> implements GPTVisionMessageContentImageUrl {
    public GPTVisionMessageContentImageUrlImpl() {
        super();
    }

    public GPTVisionMessageContentImageUrlImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public GPTVisionMessageContentImageUrl getImplementation() {
        return this;
    }
}
