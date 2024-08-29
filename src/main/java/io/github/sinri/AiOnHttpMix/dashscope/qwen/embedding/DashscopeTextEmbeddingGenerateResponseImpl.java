package io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class DashscopeTextEmbeddingGenerateResponseImpl extends UnmodifiableJsonifiableEntityImpl implements DashscopeTextEmbeddingGenerateResponse {
    private final int statusCode;

    public DashscopeTextEmbeddingGenerateResponseImpl(int statusCode, @NotNull JsonObject jsonObject) {
        super(jsonObject);
        this.statusCode = statusCode;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    public static class OutputImpl extends UnmodifiableJsonifiableEntityImpl implements DashscopeTextEmbeddingGenerateResponseOutput {

        public OutputImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class EmbeddingImpl extends UnmodifiableJsonifiableEntityImpl implements DashscopeTextEmbeddingGenerateResponseOutput.Embedding {

        public EmbeddingImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class UsageImpl extends UnmodifiableJsonifiableEntityImpl implements DashscopeTextEmbeddingGenerateResponseUsage {

        public UsageImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class TensorImpl implements DashscopeTextEmbeddingGenerateResponseOutput.Tensor {
        private @NotNull
        final List<Double> list;

        public TensorImpl(@NotNull List<Double> list) {
            this.list = list;
        }

        @Override
        public List<Double> getDoubleList() {
            return list;
        }
    }
}
