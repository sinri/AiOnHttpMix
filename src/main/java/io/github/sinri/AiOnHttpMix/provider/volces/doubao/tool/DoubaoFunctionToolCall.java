package io.github.sinri.AiOnHttpMix.provider.volces.doubao.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.0
 */
public class DoubaoFunctionToolCall extends UnmodifiableJsonifiableEntityImpl implements FunctionToolCall {
    public DoubaoFunctionToolCall(@Nonnull JsonObject jsonObject) {
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
