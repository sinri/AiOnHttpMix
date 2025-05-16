package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class QwenToolDefinition extends JsonifiableEntityImpl<ToolDefinition> implements ToolDefinition {
    public QwenToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    public QwenToolDefinition() {
        super();
    }

    public QwenToolDefinition(QwenFunctionToolDefinition qwenFunctionToolDefinition) {
        super();
        type("function");
        function(qwenFunctionToolDefinition);
    }

    public QwenToolDefinition(Handler<QwenFunctionToolDefinition> qwenFunctionToolDefinitionHandler) {
        super();
        var x = new QwenFunctionToolDefinition();
        qwenFunctionToolDefinitionHandler.handle(x);
        type("function");
        function(x);
    }

    @Override
    public FunctionToolDefinition function() {
        return new QwenFunctionToolDefinition(readJsonObject("function"));
    }

    @Override
    public @NotNull QwenToolDefinition getImplementation() {
        return this;
    }
}
