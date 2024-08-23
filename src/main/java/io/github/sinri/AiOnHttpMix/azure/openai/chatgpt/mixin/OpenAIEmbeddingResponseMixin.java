package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface OpenAIEmbeddingResponseMixin extends UnmodifiableJsonifiableEntity {
    default String getObject() {
        return readString("object");
    }

    default String getModel() {
        return readString("model");
    }

    @Nullable
    default List<Datum> getData() {
        JsonArray array = readJsonArray("data");
        if (array != null) {
            List<Datum> list = new ArrayList<>();
            array.forEach(x -> {
                list.add(new Datum((JsonObject) x));
            });
            return list;
        }
        return null;
    }

    class Datum extends SimpleJsonifiableEntity {
        public Datum(JsonObject jsonObject) {
            super(jsonObject);
        }

        public String getObject() {
            return readString("object");
        }

        public JsonArray getRawEmbedding() {
            return readJsonArray("embedding");
        }

        public List<Double> getEmbedding() {
            return readDoubleArray("embedding");
        }

        public Integer getIndex() {
            return readInteger("index");
        }

    }
}