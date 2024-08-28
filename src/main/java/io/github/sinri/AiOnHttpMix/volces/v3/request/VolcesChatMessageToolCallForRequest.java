package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.github.sinri.AiOnHttpMix.volces.v3.tool.SharedMessageToolCall;
import io.github.sinri.AiOnHttpMix.volces.v3.tool.VolcesChatFunctionDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface VolcesChatMessageToolCallForRequest extends SharedMessageToolCall, JsonifiableEntity<VolcesChatMessageToolCallForRequest> {
    static VolcesChatMessageToolCallForRequest create() {
        return new VolcesChatMessageToolCallForRequestImpl();
    }

    static VolcesChatMessageToolCallForRequest wrap(JsonObject jsonObject) {
        return new VolcesChatMessageToolCallForRequestImpl(jsonObject);
    }

    default VolcesChatMessageToolCallForRequest setId(String id) {
        this.toJsonObject().put("id", id);
        return this;
    }

    default VolcesChatMessageToolCallForRequest setType(VolcesChatFunctionDefinition.VolcesToolType type) {
        this.toJsonObject().put("type", type.name());
        return this;
    }

    default VolcesChatMessageToolCallForRequest setFunction(VolcesChatFunctionCallForRequest functionParam) {
        this.toJsonObject().put("function", functionParam.cloneAsJsonObject());
        return this;
    }
}
