package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenVLOutputMessageImpl extends UnmodifiableJsonifiableEntityImpl implements QwenVLOutputMessage {
    public QwenVLOutputMessageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
