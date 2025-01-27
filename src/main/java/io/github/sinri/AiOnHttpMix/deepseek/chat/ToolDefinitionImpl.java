package io.github.sinri.AiOnHttpMix.deepseek.chat;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ToolDefinitionImpl implements DeepseekChatRequest.ToolDefinition {
    private JsonObject jsonObject;

    public ToolDefinitionImpl() {
        this.jsonObject = new JsonObject();
    }

    public ToolDefinitionImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull DeepseekChatRequest.ToolDefinition reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
