package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenResponseOutputChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseOutputChoice {
    public QwenResponseOutputChoiceImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
