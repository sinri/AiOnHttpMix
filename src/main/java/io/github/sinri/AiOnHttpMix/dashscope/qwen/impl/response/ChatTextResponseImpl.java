package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.response;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ChatTextResponseImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.ChatTextResponse {
    public ChatTextResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public OutputForTextResponse getOutput() {
        JsonObject output = readJsonObject("output");
        Objects.requireNonNull(output);
        return new OutputForTextResponseImpl(output);
    }

    @Override
    public Usage getUsage() {
        JsonObject usage = readJsonObject("usage");
        Objects.requireNonNull(usage);
        return new UsageImpl(usage);
    }

    public static class OutputForTextResponseImpl extends UnmodifiableJsonifiableEntityImpl implements OutputForTextResponse {

        public OutputForTextResponseImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
