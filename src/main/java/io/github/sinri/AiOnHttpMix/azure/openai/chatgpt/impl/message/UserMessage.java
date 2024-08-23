package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class UserMessage implements ChatGPTKit.Message {
    private JsonObject jsonObject;

    public UserMessage(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.jsonObject.put("role", ChatCompletionRequestMessageRole.user.name());
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public ChatGPTKit.@NotNull Message reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }


}
