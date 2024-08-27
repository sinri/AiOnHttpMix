package io.github.sinri.AiOnHttpMix.dashscope.qwen.embedding;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface DashscopeTextEmbeddingGenerateResponseOutput extends UnmodifiableJsonifiableEntity {
    static DashscopeTextEmbeddingGenerateResponseOutput wrap(JsonObject jsonObject) {
        return new DashscopeTextEmbeddingGenerateResponseImpl.OutputImpl(jsonObject);
    }

    default List<Embedding> getEmbeddings() {
        List<JsonObject> embeddings = readJsonObjectArray("embeddings");
        if (embeddings == null) return null;
        return embeddings.stream().map(Embedding::wrap).toList();
    }

    interface Embedding extends UnmodifiableJsonifiableEntity {
        static Embedding wrap(JsonObject embedding) {
            return new DashscopeTextEmbeddingGenerateResponseImpl.EmbeddingImpl(embedding);
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
            return new DashscopeTextEmbeddingGenerateResponseImpl.TensorImpl(list);
        }

        List<Double> getDoubleList();

        default JsonArray toJsonArray() {
            return new JsonArray(getDoubleList());
        }
    }
}
