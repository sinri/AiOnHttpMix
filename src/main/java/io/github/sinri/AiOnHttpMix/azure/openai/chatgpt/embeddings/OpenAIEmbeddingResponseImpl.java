package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.embeddings;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class OpenAIEmbeddingResponseImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIEmbeddingResponse {
    public OpenAIEmbeddingResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
