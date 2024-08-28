package io.github.sinri.AiOnHttpMix.volces.v3.tool;

import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatFunctionCallForRequest;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface SharedMessageToolCall extends UnmodifiableJsonifiableEntity {

    default String getId() {
        return readString("id");
    }

    default VolcesChatFunctionDefinition.VolcesToolType getType() {
        return VolcesChatFunctionDefinition.VolcesToolType.valueOf(readString("type"));
    }

    @Nullable
    default VolcesChatFunctionCallForRequest getFunction() {
        JsonObject x = readJsonObject("function");
        if (x == null) return null;
        return VolcesChatFunctionCallForRequest.wrap(x);
    }
}
