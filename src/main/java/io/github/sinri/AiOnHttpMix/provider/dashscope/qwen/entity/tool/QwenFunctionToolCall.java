package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class QwenFunctionToolCall extends UnmodifiableJsonifiableEntityImpl implements FunctionToolCall {
    public QwenFunctionToolCall(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public String getName() {
        return readString("name");
    }

    @Override
    public String getArguments() {
        return readString("arguments");
    }
}
