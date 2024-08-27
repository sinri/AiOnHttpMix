package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class OpenAIChatGptToolDefinitionImpl implements OpenAIChatGptToolDefinition {
    private JsonObject jsonObject;

    public OpenAIChatGptToolDefinitionImpl(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public @NotNull OpenAIChatGptToolDefinition reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

}
