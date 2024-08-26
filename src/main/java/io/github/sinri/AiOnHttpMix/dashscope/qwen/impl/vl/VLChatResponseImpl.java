package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class VLChatResponseImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.VLChatResponse {
    public VLChatResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class OutputImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.VLChatResponse.Output {
        public OutputImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class ChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.VLChatResponse.Choice {
        public ChoiceImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
