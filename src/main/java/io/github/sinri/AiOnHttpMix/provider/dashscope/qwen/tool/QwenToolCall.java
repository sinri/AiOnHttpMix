package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

import javax.annotation.Nonnull;
/**
 * @since 2.0.0
 */
public class QwenToolCall extends UnmodifiableJsonifiableEntityImpl implements ToolCall {
    public QwenToolCall(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public String getType() {
        return readString("type");
    }

    @Override
    public String getId() {
        return readString("id");
    }

    @Override
    public Integer getIndex() {
        return readInteger("index");
    }

    @Override
    public FunctionToolCall getFunction() {
        JsonObject x = readJsonObject("function");
        Objects.requireNonNull(x);
        return new QwenFunctionToolCall(x);
    }
}
