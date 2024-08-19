package io.github.sinri.AiOnHttpMix.azure.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExtenstionChatCompletionsParameters extends ChatCompletionsParameters {
    public ExtenstionChatCompletionsParameters() {
        super();
    }

    public ExtenstionChatCompletionsParameters(JsonObject another) {
        super(another);

        List<String> keys = new ArrayList<>();
        keys.add("dataSources");
        keys.forEach(key -> {
            if (another.containsKey(key)) {
                jsonObject.put(key, another.getValue(key));
            }
        });
    }

    public ChatCompletionsParameters setDataSources(Collection<DataSource> dataSources) {
        JsonArray array = new JsonArray();
        dataSources.forEach(ds -> {
            array.add(ds.toJsonObject());
        });
        this.jsonObject.put("dataSources", array);
        return this;
    }

    public boolean requireDataSource() {
        var x = this.jsonObject.getJsonArray("dataSources");
        if (x == null) return false;
        return !x.isEmpty();
    }

    public static class DataSource extends SimpleJsonifiableEntity {
        public DataSource() {
            this(new JsonObject());
        }

        public DataSource(JsonObject jsonObject) {
            super(jsonObject);
        }

        /**
         * @param type commonly `AzureCognitiveSearch`
         */
        public DataSource setType(String type) {
            this.jsonObject.put("type", type);
            return this;
        }

        public DataSource setParameter(String key, String value) {
            if (!this.jsonObject.containsKey("parameters")) {
                this.jsonObject.put("parameters", new JsonObject());
            }
            this.jsonObject.getJsonObject("parameters").put(key, value);
            return this;
        }
    }

    public static class DataSourceAsAzureCognitiveSearch extends DataSource {

        public DataSourceAsAzureCognitiveSearch() {
            this(new JsonObject());
        }

        public DataSourceAsAzureCognitiveSearch(JsonObject parameters) {
            super(new JsonObject()
                    .put("type", "AzureCognitiveSearch")
                    .put("parameters", parameters)
            );
        }
    }
}
