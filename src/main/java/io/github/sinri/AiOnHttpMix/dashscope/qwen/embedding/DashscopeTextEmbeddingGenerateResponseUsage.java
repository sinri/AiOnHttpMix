package io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface DashscopeTextEmbeddingGenerateResponseUsage extends UnmodifiableJsonifiableEntity {
    static DashscopeTextEmbeddingGenerateResponseUsage wrap(JsonObject usage) {
        return new DashscopeTextEmbeddingGenerateResponseImpl.UsageImpl(usage);
    }

    default Integer getTotalTokens() {
        return readInteger("total_tokens");
    }
}
