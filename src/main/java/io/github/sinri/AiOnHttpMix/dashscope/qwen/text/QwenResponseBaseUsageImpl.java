package io.github.sinri.AiOnHttpMix.dashscope.qwen.text;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenResponseBaseUsageImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseBase.Usage {
    public QwenResponseBaseUsageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
