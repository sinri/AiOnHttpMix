package io.github.sinri.AiOnHttpMix.moonshot.kimi.impl;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.mixin.KimiUsageMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class KimiUsageImpl extends UnmodifiableJsonifiableEntityImpl implements KimiUsageMixin {
    public KimiUsageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
