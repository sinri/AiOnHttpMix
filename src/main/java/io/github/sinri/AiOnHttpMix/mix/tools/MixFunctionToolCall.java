package io.github.sinri.AiOnHttpMix.mix.tools;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class MixFunctionToolCall extends JsonifiableEntityImpl<MixFunctionToolCall>
        implements FunctionToolCall {

    public MixFunctionToolCall(JsonObject jsonObject) {
        super(jsonObject);
    }

    public static MixFunctionToolCall from(FunctionToolCall functionToolCall) {
        return new MixFunctionToolCall(new JsonObject()
                .put("name", functionToolCall.getName())
                .put("arguments", functionToolCall.getArguments()));
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
    public MixFunctionToolCall getImplementation() {
        return this;
    }
}
