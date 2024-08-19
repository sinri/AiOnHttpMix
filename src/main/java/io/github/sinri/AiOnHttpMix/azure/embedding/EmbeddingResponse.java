package io.github.sinri.AiOnHttpMix.azure.embedding;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class EmbeddingResponse extends SimpleJsonifiableEntity {
    public EmbeddingResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getObject() {
        return readString("object");
    }

    public String getModel() {
        return readString("model");
    }

    public List<Datum> getData() {
        List<Datum> list = new ArrayList<>();
        readJsonArray("data").forEach(x -> {
            list.add(new Datum((JsonObject) x));
        });
        return list;
    }

    public static class Datum extends SimpleJsonifiableEntity {
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
