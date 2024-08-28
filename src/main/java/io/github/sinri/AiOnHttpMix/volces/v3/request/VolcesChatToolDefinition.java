package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.volces.v3.tool.VolcesChatFunctionDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface VolcesChatToolDefinition extends JsonifiableEntity<VolcesChatToolDefinition> {
    static VolcesChatToolDefinition create(VolcesChatFunctionDefinition functionDefinition) {
        return new VolcesChatToolDefinitionImpl(functionDefinition.toJsonObject());
    }

    static VolcesChatToolDefinition create() {
        return new VolcesChatToolDefinitionImpl();
    }

    static VolcesChatToolDefinition wrap(JsonObject jsonObject) {
        return new VolcesChatToolDefinitionImpl(jsonObject);
    }

    static VolcesChatFunctionDefinition buildFunction(Handler<FunctionToolDefinition.FunctionToolDefinitionBuilder<VolcesChatFunctionDefinition.Builder, VolcesChatFunctionDefinition>> handler) {
        FunctionToolDefinition.FunctionToolDefinitionBuilder<VolcesChatFunctionDefinition.Builder, VolcesChatFunctionDefinition> builder = VolcesChatFunctionDefinition.builder();
        handler.handle(builder);
        return builder.build();
    }

    default VolcesChatFunctionDefinition.VolcesToolType getType() {
        return VolcesChatFunctionDefinition.VolcesToolType.valueOf(readString("type"));
    }

    default VolcesChatToolDefinition setType(VolcesChatFunctionDefinition.VolcesToolType type) {
        this.toJsonObject().put("type", type);
        return this;
    }

    default VolcesChatFunctionDefinition getFunctionDefinition() {
        JsonObject x = readJsonObject("function");
        if (x == null) return null;
        return VolcesChatFunctionDefinition.wrap(x);
    }

//    default E setFunctionDefinition(VolcesKit.FunctionDefinition functionDefinition) {
//        this.toJsonObject().put("function", functionDefinition.toJsonObject());
//        return getImplementation();
//    }
}
