package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface OpenAIChatGptMessageContentPart extends JsonifiableEntity<OpenAIChatGptMessageContentPart> {
    static OpenAIChatGptMessageContentPart withText(String text) {
        return new ChatCompletionRequestMessageContentPartImpl(new JsonObject()
                .put("type", OpenAIChatGptMessage.ChatCompletionRequestMessageContentPartType.text.name())
                .put("text", text)
        );
    }

    static OpenAIChatGptMessageContentPart withImage(String url) {
        return new ChatCompletionRequestMessageContentPartImpl(new JsonObject()
                .put("type", OpenAIChatGptMessage.ChatCompletionRequestMessageContentPartType.image_url.name())
                .put("url", url)
        );
    }

    static OpenAIChatGptMessageContentPart withImage(String url, @NotNull ImageDetailLevel imageDetailLevel) {
        return new ChatCompletionRequestMessageContentPartImpl(new JsonObject()
                .put("type", OpenAIChatGptMessage.ChatCompletionRequestMessageContentPartType.image_url.name())
                .put("url", url)
                .put("detail", imageDetailLevel.name())
        );
    }

    /**
     * Specifies the detail level of the image.
     */
    enum ImageDetailLevel {
        auto, low, high
    }
}
