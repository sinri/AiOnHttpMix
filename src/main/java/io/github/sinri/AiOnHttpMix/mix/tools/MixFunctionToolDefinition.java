package io.github.sinri.AiOnHttpMix.mix.tools;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class MixFunctionToolDefinition extends JsonifiableEntityImpl<FunctionToolDefinition> implements FunctionToolDefinition {
    public MixFunctionToolDefinition() {
        super();
    }

    public MixFunctionToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public FunctionToolDefinition getImplementation() {
        return this;
    }
}
