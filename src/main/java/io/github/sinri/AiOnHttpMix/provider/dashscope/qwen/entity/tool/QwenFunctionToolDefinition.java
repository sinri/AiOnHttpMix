package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.0
 */
public class QwenFunctionToolDefinition extends JsonifiableEntityImpl<FunctionToolDefinition> implements FunctionToolDefinition {
    public QwenFunctionToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    public QwenFunctionToolDefinition() {
        super();
    }

    @Override
    public @Nonnull QwenFunctionToolDefinition getImplementation() {
        return this;
    }
}
