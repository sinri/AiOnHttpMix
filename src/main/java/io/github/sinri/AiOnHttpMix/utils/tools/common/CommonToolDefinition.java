package io.github.sinri.AiOnHttpMix.utils.tools.common;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * 通用工具定义实现。
 * 
 * @since 2.0.0
 */
public class CommonToolDefinition extends JsonifiableEntityImpl<ToolDefinition> implements ToolDefinition {
    public CommonToolDefinition() {
        this(new JsonObject());
    }

    public CommonToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    public CommonToolDefinition(FunctionToolDefinition functionToolDefinition) {
        super(new JsonObject());
        function(functionToolDefinition);
    }

    @Override
    public FunctionToolDefinition function() {
        JsonObject f = readJsonObject("function");
        if (f == null) return null;
        return new CommonFunctionToolDefinition(f);
    }

    @Nonnull
    @Override
    public ToolDefinition getImplementation() {
        return this;
    }
}
