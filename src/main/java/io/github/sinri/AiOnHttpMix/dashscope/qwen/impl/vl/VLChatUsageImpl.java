package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.vl;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class VLChatUsageImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.VLChatUsage {
    public VLChatUsageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
