package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class OpenAIChatGptResponseToolCallImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIChatGptResponseToolCall {
    public OpenAIChatGptResponseToolCallImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nullable
    public OpenAIChatGptResponseFunctionCall getFunction() {
        JsonObject x = readJsonObject("function");
        if (x == null) return null;
        return new OpenAIChatGptResponseFunctionCallImpl(x);
    }
}
