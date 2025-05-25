package io.github.sinri.AiOnHttpMix.utils.tools.common;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionParameterDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import java.util.List;

public class CommonFunctionToolDefinition extends JsonifiableEntityImpl<FunctionToolDefinition> implements FunctionToolDefinition {
    public CommonFunctionToolDefinition() {
        this(new JsonObject());
    }

    public CommonFunctionToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    public CommonFunctionToolDefinition(
            String name,
            String desc,
            List<FunctionParameterDefinition> parameterDefinitions
    ) {
        super(new JsonObject());
        name(name);
        description(desc);
        parameters(parameterDefinitions);
    }

    @Nonnull
    @Override
    public FunctionToolDefinition getImplementation() {
        return this;
    }
}
