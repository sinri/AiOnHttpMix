package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.embeddings;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class EmbeddingResponseImpl extends UnmodifiableJsonifiableEntityImpl implements ChatGPTKit.EmbeddingResponse {
    public EmbeddingResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
