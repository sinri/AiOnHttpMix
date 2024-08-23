package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class VLChatOutputMessageImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.VLChatOutputMessage {
    public VLChatOutputMessageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
