package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.embedding;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextEmbeddingGenerateResponseImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.TextEmbeddingGenerateResponse {
    public TextEmbeddingGenerateResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class OutputImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.TextEmbeddingGenerateResponse.Output {

        public OutputImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class EmbeddingImpl extends UnmodifiableJsonifiableEntityImpl implements Output.Embedding {

        public EmbeddingImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class UsageImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.TextEmbeddingGenerateResponse.Usage {

        public UsageImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class TensorImpl implements Output.Tensor {
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
