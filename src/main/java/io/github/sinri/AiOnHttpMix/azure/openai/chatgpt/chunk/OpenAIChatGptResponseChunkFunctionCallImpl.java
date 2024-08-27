package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class OpenAIChatGptResponseChunkFunctionCallImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIChatGptResponseChunkFunctionCall {

    public OpenAIChatGptResponseChunkFunctionCallImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
