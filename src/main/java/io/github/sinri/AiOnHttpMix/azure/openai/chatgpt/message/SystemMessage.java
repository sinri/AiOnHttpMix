package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class SystemMessage implements OpenAIChatGptMessage {
    private JsonObject jsonObject;

    public SystemMessage(String name, String content) {
        super();
        jsonObject = new JsonObject()
                .put("role", ChatCompletionRequestMessageRole.system.name())
                .put("name", name)
                .put("content", content);
    }

    public SystemMessage(String content) {
        super();
        jsonObject = new JsonObject()
                .put("role", ChatCompletionRequestMessageRole.system.name())
                .put("content", content);
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
