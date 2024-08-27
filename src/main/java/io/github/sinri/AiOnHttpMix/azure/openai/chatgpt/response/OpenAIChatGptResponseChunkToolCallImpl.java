package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkFunctionCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class OpenAIChatGptResponseChunkToolCallImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIChatGptResponseToolCall {
    public OpenAIChatGptResponseChunkToolCallImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nullable
    public OpenAIChatGptResponseChunkFunctionCall getFunction() {
        JsonObject x = readJsonObject("function");
        if (x == null) return null;
        return OpenAIChatGptResponseChunkFunctionCall.wrap(x);
    }
}
