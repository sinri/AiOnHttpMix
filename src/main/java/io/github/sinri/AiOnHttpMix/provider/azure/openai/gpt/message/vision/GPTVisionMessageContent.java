package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision;

import io.github.sinri.AiOnHttpMix.utils.vision.openai.OpenAICompatibleVisionContent;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public interface GPTVisionMessageContent extends OpenAICompatibleVisionContent<GPTVisionMessageContent, GPTVisionMessageContentImageUrl> {
    static GPTVisionMessageContent create() {
        return new GPTVisionMessageContentImpl();
    }

    static GPTVisionMessageContent wrap(JsonObject jsonObject) {
        return new GPTVisionMessageContentImpl(jsonObject);
    }

    default GPTVisionMessageContentImageUrl getImageUrl() {
        JsonObject x = readJsonObject("image_url");
        Objects.requireNonNull(x);
        return GPTVisionMessageContentImageUrl.wrap(x);
    }
}
