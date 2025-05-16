package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenResponseImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponse {
    public QwenResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
