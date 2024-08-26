package io.github.sinri.AiOnHttpMix.volces.v3.mixin.request;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface VolcesToolParamMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {

    default E setType(VolcesKit.VolcesToolType type) {
        this.toJsonObject().put("type", type);
        return getImplementation();
    }

    default VolcesKit.VolcesToolType getType() {
        return VolcesKit.VolcesToolType.valueOf(readString("type"));
    }

    default VolcesKit.FunctionDefinition getFunctionDefinition() {
        JsonObject x = readJsonObject("function");
        if (x == null) return null;
        return VolcesKit.FunctionDefinition.wrap(x);
    }

//    default E setFunctionDefinition(VolcesKit.FunctionDefinition functionDefinition) {
//        this.toJsonObject().put("function", functionDefinition.toJsonObject());
//        return getImplementation();
//    }
}
