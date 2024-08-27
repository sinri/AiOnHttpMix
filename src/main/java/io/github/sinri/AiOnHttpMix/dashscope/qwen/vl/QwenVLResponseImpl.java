package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenVLResponseImpl extends UnmodifiableJsonifiableEntityImpl implements QwenVLResponse {
    public QwenVLResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class OutputImpl extends UnmodifiableJsonifiableEntityImpl implements QwenVLResponse.Output {
        public OutputImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class ChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements QwenVLResponse.Choice {
        public ChoiceImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
