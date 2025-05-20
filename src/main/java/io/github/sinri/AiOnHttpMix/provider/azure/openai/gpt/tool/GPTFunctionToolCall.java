package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class GPTFunctionToolCall extends UnmodifiableJsonifiableEntityImpl implements FunctionToolCall {
    public GPTFunctionToolCall(@Nonnull JsonObject jsonObject) {
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
