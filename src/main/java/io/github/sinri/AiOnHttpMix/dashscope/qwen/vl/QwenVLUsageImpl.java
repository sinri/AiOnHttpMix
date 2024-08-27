package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenVLUsageImpl extends UnmodifiableJsonifiableEntityImpl implements QwenVLUsage {
    public QwenVLUsageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
