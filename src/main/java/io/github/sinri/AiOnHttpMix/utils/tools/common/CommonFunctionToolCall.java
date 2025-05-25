package io.github.sinri.AiOnHttpMix.utils.tools.common;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class CommonFunctionToolCall extends JsonifiableEntityImpl<FunctionToolCall> implements FunctionToolCall {
    public CommonFunctionToolCall(String name, String arguments) {
        this(new JsonObject()
                .put("name", name)
                .put("arguments", arguments)
        );
    }

    public CommonFunctionToolCall(JsonObject jsonObject) {
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

    @Nonnull
    @Override
    public FunctionToolCall getImplementation() {
        return this;
    }
}
