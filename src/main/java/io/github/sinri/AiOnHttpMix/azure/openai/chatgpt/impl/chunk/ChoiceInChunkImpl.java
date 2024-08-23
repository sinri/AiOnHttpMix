package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIResponseChunkMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChoiceInChunkImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIResponseChunkMixin.ChoiceInChunk {
    public ChoiceInChunkImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public @Nullable OpenAIResponseChunkMixin.Delta getDelta() {
        JsonObject delta = readJsonObject("delta");
        if (delta == null) return null;
        return new DeltaImpl(delta);
    }
}
