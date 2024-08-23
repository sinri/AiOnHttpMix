package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.response;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.ChatResponseBase;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class UsageImpl extends UnmodifiableJsonifiableEntityImpl implements ChatResponseBase.Usage {
    public UsageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
