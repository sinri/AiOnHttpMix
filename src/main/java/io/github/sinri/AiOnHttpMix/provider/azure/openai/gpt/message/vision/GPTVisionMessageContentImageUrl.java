package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision;

import io.github.sinri.AiOnHttpMix.utils.vision.openai.OpenAICompatibleVisionContentForImageUrl;
import io.vertx.core.json.JsonObject;

public interface GPTVisionMessageContentImageUrl extends OpenAICompatibleVisionContentForImageUrl<GPTVisionMessageContentImageUrl> {
    static GPTVisionMessageContentImageUrl create() {
        return new GPTVisionMessageContentImageUrlImpl();
    }

    static GPTVisionMessageContentImageUrl wrap(JsonObject jsonObject) {
        return new GPTVisionMessageContentImageUrlImpl(jsonObject);
    }
}
