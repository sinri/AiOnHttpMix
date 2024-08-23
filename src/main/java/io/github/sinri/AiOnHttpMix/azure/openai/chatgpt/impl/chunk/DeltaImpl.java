package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIResponseChunkMixin;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class DeltaImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIResponseChunkMixin.Delta {
    public DeltaImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
