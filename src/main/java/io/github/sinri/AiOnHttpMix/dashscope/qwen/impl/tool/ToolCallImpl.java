package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.tool;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ToolCallImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.ToolCall {
    public ToolCallImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public FunctionCall getFunction() {
        JsonObject function = readJsonObject("function");
        if (function == null) return null;
        return new FunctionCallImpl(function);
    }

    public static class FunctionCallImpl extends UnmodifiableJsonifiableEntityImpl implements FunctionCall {

        public FunctionCallImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
