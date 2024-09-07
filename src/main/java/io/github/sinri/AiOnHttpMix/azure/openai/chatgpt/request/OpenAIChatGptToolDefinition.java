package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface OpenAIChatGptToolDefinition extends JsonifiableEntity<OpenAIChatGptToolDefinition> {
    static OpenAIChatGptToolDefinition wrap(JsonObject jsonObject) {
        return new OpenAIChatGptToolDefinitionImpl(jsonObject);
    }

    static OpenAIChatGptToolDefinition.Builder builder() {
        return new OpenAIChatGptToolDefinition.Builder();
    }

    enum Type {
        function
    }

    class Builder extends FunctionToolDefinition.FunctionToolDefinitionBuilder<Builder, OpenAIChatGptToolDefinition> {

        @Override
        public OpenAIChatGptToolDefinition build() {
            return new OpenAIChatGptToolDefinitionImpl(toJsonObject());
        }

        @Override
        public @NotNull Builder getImplementation() {
            return this;
        }
    }
}
