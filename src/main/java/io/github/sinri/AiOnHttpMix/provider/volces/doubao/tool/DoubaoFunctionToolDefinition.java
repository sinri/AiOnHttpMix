package io.github.sinri.AiOnHttpMix.provider.volces.doubao.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.0
 */
public class DoubaoFunctionToolDefinition extends JsonifiableEntityImpl<FunctionToolDefinition> implements FunctionToolDefinition {
    public DoubaoFunctionToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    public DoubaoFunctionToolDefinition() {
        super();
    }

    @Override
    public @Nonnull DoubaoFunctionToolDefinition getImplementation() {
        return this;
    }
}
