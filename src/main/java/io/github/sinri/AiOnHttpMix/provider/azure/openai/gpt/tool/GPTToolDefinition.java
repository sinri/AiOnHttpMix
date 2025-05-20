package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class GPTToolDefinition extends JsonifiableEntityImpl<ToolDefinition> implements ToolDefinition {
    public GPTToolDefinition() {
        super();
    }

    public GPTToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public FunctionToolDefinition function() {
        JsonObject j = readJsonObject("function");
        if (j == null) {
            j = new JsonObject();
        }
        return new GPTFunctionToolDefinition(j);
    }

    @Nonnull
    @Override
    public ToolDefinition getImplementation() {
        return this;
    }
}
