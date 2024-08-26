package io.github.sinri.AiOnHttpMix.volces.v3.mixin.tool;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface SharedMessageToolCall extends UnmodifiableJsonifiableEntity {

    default String getId() {
        return readString("id");
    }

    default VolcesKit.VolcesToolType getType() {
        return VolcesKit.VolcesToolType.valueOf(readString("type"));
    }

    @Nullable
    default VolcesKit.FunctionParam getFunction() {
        JsonObject x = readJsonObject("function");
        if (x == null) return null;
        return VolcesKit.FunctionParam.wrap(x);
    }
}
