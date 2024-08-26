package io.github.sinri.AiOnHttpMix.volces.v3.mixin.request;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.AiOnHttpMix.volces.v3.mixin.tool.SharedMessageToolCall;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface VolcesChatMessageToolCallParamMixin<E> extends SharedMessageToolCall, JsonifiableEntity<E>, SelfInterface<E> {
    default E setId(String id) {
        this.toJsonObject().put("id", id);
        return getImplementation();
    }

    default E setType(VolcesKit.VolcesToolType type) {
        this.toJsonObject().put("type", type.name());
        return getImplementation();
    }

    default E setFunction(VolcesKit.FunctionParam functionParam) {
        this.toJsonObject().put("function", functionParam.cloneAsJsonObject());
        return getImplementation();
    }
}
