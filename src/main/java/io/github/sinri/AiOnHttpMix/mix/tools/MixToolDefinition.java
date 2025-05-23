package io.github.sinri.AiOnHttpMix.mix.tools;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class MixToolDefinition extends JsonifiableEntityImpl<ToolDefinition> implements ToolDefinition {

    public MixToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    public MixToolDefinition(FunctionToolDefinition mixFunctionToolDefinition) {
        super();
        function(mixFunctionToolDefinition);
    }

    @Override
    public FunctionToolDefinition function() {
        return new MixFunctionToolDefinition(readJsonObject("function"));
    }

    @Nonnull
    @Override
    public MixToolDefinition getImplementation() {
        return this;
    }
}
