package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.embedding;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.embedding.TextEmbeddingGenerateResponseImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface DashscopeTextEmbeddingGenerateResponseMixin extends UnmodifiableJsonifiableEntity {
    default Output getOutput() {
        var x = readJsonObject("output");
        if (x == null) return null;
        return Output.wrap(x);
    }

    default Usage getUsage() {
        var x = readJsonObject("usage");
        if (x == null) return null;
        return Usage.wrap(x);
    }

    default String getRequestId() {
        return readString("requestId");
    }

    interface Output extends UnmodifiableJsonifiableEntity {
        static Output wrap(JsonObject jsonObject) {
            return new TextEmbeddingGenerateResponseImpl.OutputImpl(jsonObject);
        }

        default List<Embedding> getEmbeddings() {
            List<JsonObject> embeddings = readJsonObjectArray("embeddings");
            if (embeddings == null) return null;
            return embeddings.stream().map(Embedding::wrap).toList();
        }

        interface Embedding extends UnmodifiableJsonifiableEntity {
            static Embedding wrap(JsonObject embedding) {
                return new TextEmbeddingGenerateResponseImpl.EmbeddingImpl(embedding);
            }

            default Integer getTextIndex() {
                return readInteger("text_index");
            }

            default Tensor getTensor() {
                return Tensor.wrap(readDoubleArray("embedding"));
            }
        }

        interface Tensor {
            static Tensor wrap(List<Double> list) {
                return new TextEmbeddingGenerateResponseImpl.TensorImpl(list);
            }

            List<Double> getDoubleList();

            default JsonArray toJsonArray() {
                return new JsonArray(getDoubleList());
            }
        }
    }

    interface Usage extends UnmodifiableJsonifiableEntity {
        static Usage wrap(JsonObject usage) {
            return new TextEmbeddingGenerateResponseImpl.UsageImpl(usage);
        }

        default Integer getTotalTokens() {
            return readInteger("total_tokens");
        }
    }
}
