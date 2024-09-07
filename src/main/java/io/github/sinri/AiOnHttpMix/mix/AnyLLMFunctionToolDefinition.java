package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class AnyLLMFunctionToolDefinition implements FunctionToolDefinition<AnyLLMFunctionToolDefinition> {
    private JsonObject jsonObject;

    public AnyLLMFunctionToolDefinition() {
        jsonObject = new JsonObject();
    }

    static Builder builder() {
        return new Builder();
    }

    static AnyLLMFunctionToolDefinition wrap(JsonObject jsonObject) {
        return new AnyLLMFunctionToolDefinition().reloadDataFromJsonObject(jsonObject);
    }

    @Override
    public @NotNull AnyLLMFunctionToolDefinition getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public @NotNull AnyLLMFunctionToolDefinition reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public static class Builder extends FunctionToolDefinitionBuilder<Builder, AnyLLMFunctionToolDefinition> {

        @Override
        public AnyLLMFunctionToolDefinition build() {
            return new AnyLLMFunctionToolDefinition().reloadDataFromJsonObject(toJsonObject());
        }

        @Override
        public @NotNull Builder getImplementation() {
            return this;
        }
    }
}
