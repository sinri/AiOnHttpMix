package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChatCompletionRequestMessageContentPartImpl implements ChatGPTKit.Message.ChatCompletionRequestMessageContentPart {
    private JsonObject jsonObject;

    public ChatCompletionRequestMessageContentPartImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public ChatGPTKit.Message.@NotNull ChatCompletionRequestMessageContentPart reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject=jsonObject;
        return this;
    }
}
