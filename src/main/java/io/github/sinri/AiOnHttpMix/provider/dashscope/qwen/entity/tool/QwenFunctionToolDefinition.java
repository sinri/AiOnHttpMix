package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class QwenFunctionToolDefinition extends JsonifiableEntityImpl<FunctionToolDefinition> implements FunctionToolDefinition {
    public QwenFunctionToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    public QwenFunctionToolDefinition() {
        super();
    }

    @Override
    public @NotNull QwenFunctionToolDefinition getImplementation() {
        return this;
    }
}
