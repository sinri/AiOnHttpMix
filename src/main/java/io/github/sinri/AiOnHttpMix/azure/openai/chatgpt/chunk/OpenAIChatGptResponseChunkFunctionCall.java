package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.vertx.core.json.JsonObject;

public interface OpenAIChatGptResponseChunkFunctionCall extends OpenAIChatGptResponseFunctionCall {
    static OpenAIChatGptResponseChunkFunctionCall wrap(JsonObject jsonObject) {
        return new OpenAIChatGptResponseChunkFunctionCallImpl(jsonObject);
    }

    default String getArgumentsStreamPiece() {
        return readString("arguments");
    }
}
