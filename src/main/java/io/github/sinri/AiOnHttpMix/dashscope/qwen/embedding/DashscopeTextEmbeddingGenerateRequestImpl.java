package io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class DashscopeTextEmbeddingGenerateRequestImpl implements DashscopeTextEmbeddingGenerateRequest {

    private JsonObject jsonObject;

    public DashscopeTextEmbeddingGenerateRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public DashscopeTextEmbeddingGenerateRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull DashscopeTextEmbeddingGenerateRequestImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Override
    public @NotNull DashscopeTextEmbeddingGenerateRequestImpl getImplementation() {
        return this;
    }
}
