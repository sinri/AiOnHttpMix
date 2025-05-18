package io.github.sinri.AiOnHttpMix.provider.volces.doubao.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
/**
 * @since 2.0.0
 */
public class DoubaoToolDefinition extends JsonifiableEntityImpl<ToolDefinition> implements ToolDefinition {
    public DoubaoToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    public DoubaoToolDefinition() {
        super();
    }

    public DoubaoToolDefinition(DoubaoFunctionToolDefinition doubaoFunctionToolDefinition) {
        super();
        type("function");
        function(doubaoFunctionToolDefinition);
    }

    public DoubaoToolDefinition(Handler<DoubaoFunctionToolDefinition> doubaoFunctionToolDefinitionHandler) {
        super();
        var x = new DoubaoFunctionToolDefinition();
        doubaoFunctionToolDefinitionHandler.handle(x);
        type("function");
        function(x);
    }

    @Override
    public FunctionToolDefinition function() {
        return new DoubaoFunctionToolDefinition(readJsonObject("function"));
    }

    @Override
    public @Nonnull DoubaoToolDefinition getImplementation() {
        return this;
    }
}
