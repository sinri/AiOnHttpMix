package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIToolCallMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class FunctionCallInChunkImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIToolCallMixin.FunctionCallInChunk {

    public FunctionCallInChunkImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
