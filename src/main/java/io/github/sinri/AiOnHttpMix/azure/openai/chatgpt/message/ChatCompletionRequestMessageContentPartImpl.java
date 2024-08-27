package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class ChatCompletionRequestMessageContentPartImpl implements OpenAIChatGptMessageContentPart {
    private JsonObject jsonObject;

    public ChatCompletionRequestMessageContentPartImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull OpenAIChatGptMessageContentPart reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
