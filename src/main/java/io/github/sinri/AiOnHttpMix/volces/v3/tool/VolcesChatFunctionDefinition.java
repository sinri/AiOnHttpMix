package io.github.sinri.AiOnHttpMix.volces.v3.tool;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface VolcesChatFunctionDefinition extends FunctionToolDefinition<VolcesChatFunctionDefinition> {
    static VolcesChatFunctionDefinition wrap(JsonObject jsonObject) {
        return new VolcesChatFunctionDefinitionImpl(jsonObject);
    }

    static FunctionToolDefinitionBuilder<VolcesChatFunctionDefinition.Builder, VolcesChatFunctionDefinition> builder() {
        return new VolcesChatFunctionDefinition.Builder();
    }

    enum VolcesToolType {
        function
    }

    class Builder extends FunctionToolDefinitionBuilder<Builder, VolcesChatFunctionDefinition> {

        @Override
        public VolcesChatFunctionDefinition build() {
            return new VolcesChatFunctionDefinitionImpl(toJsonObject());
        }

        @Override
        public @NotNull Builder getImplementation() {
            return this;
        }
    }
}
