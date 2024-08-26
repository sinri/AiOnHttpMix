package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class FunctionParamImpl extends UnmodifiableJsonifiableEntityImpl implements VolcesKit.FunctionParam {
    public FunctionParamImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
