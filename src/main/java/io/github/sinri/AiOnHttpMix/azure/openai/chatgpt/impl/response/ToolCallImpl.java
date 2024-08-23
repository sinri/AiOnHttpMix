package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.response;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ToolCallImpl extends UnmodifiableJsonifiableEntityImpl implements ChatGPTKit.ToolCall {
    public ToolCallImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nullable
    public FunctionCall getFunction() {
        JsonObject x = readJsonObject("function");
        if (x == null) return null;
        return new FunctionCallImpl(x);
    }
}
