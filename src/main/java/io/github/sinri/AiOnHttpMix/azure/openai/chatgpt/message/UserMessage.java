package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class UserMessage implements OpenAIChatGptMessage {
    private JsonObject jsonObject;

    public UserMessage(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.jsonObject.put("role", ChatGptRole.user.name());
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull OpenAIChatGptMessage reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }


}
