package io.github.sinri.AiOnHttpMix.utils.tools.common;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class CommonToolCall extends JsonifiableEntityImpl<ToolCall> implements ToolCall {
    public CommonToolCall(JsonObject jsonObject) {
        super(jsonObject);
    }

    public CommonToolCall(String id, Integer index, FunctionToolCall functionToolCall) {
        this(new JsonObject()
                .put("id", id)
                .put("index", index)
                .put("type", "function")
                .put("function", functionToolCall == null ? null : functionToolCall.toJsonObject())
        );
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
            return new CommonFunctionToolCall(a);
        }
    }

    @Nonnull
    @Override
    public ToolCall getImplementation() {
        return this;
    }
}
