package io.github.sinri.AiOnHttpMix.mix.tools;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class MixToolCall extends JsonifiableEntityImpl<MixToolCall> implements ToolCall {
    public MixToolCall(JsonObject jsonObject) {
        super(jsonObject);
    }

    public static MixToolCall from(ToolCall toolCall) {
        return new MixToolCall(new JsonObject()
                .put("type", toolCall.getType())
                .put("id", toolCall.getId())
                .put("index", toolCall.getIndex())
                .put("function", toolCall.getFunction() == null ? null
                        : MixFunctionToolCall.from(toolCall.getFunction()).toJsonObject()));
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
        var a = readJsonObject("function");
        if (a == null) {
            return null;
        } else {
            return new MixFunctionToolCall(a);
        }
    }

    @Nonnull
    @Override
    public MixToolCall getImplementation() {
        return this;
    }
}
