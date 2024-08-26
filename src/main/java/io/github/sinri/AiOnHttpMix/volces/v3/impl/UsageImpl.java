package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class UsageImpl extends UnmodifiableJsonifiableEntityImpl implements VolcesKit.Usage {
    public UsageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
