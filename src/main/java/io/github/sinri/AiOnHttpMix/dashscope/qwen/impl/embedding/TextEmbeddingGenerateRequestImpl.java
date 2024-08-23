package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.embedding;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextEmbeddingGenerateRequestImpl implements QwenKit.TextEmbeddingGenerateRequest {

    private JsonObject jsonObject;

    public TextEmbeddingGenerateRequestImpl() {
        this.jsonObject = new JsonObject();
    }

    public TextEmbeddingGenerateRequestImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public TextEmbeddingGenerateRequestImpl reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Override
    public @NotNull TextEmbeddingGenerateRequestImpl getImplementation() {
        return this;
    }
}
