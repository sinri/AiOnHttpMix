package io.github.sinri.AiOnHttpMix.azure.completion;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CompletionResponse extends SimpleJsonifiableEntity {
    public CompletionResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getId() {
        return this.readString("id");
    }

    public String getObject() {
        return this.readString("object");
    }

    public Long getCreated() {
        return this.readLong("created");
    }

    public String getModel() {
        return this.readString("model");
    }

    public JsonArray getChoicesArray() {
        return this.readJsonArray("choices");
    }

    public List<CompletionChoice> getChoices() {
        List<CompletionChoice> list = new ArrayList<>();
        getChoicesArray().forEach(x -> {
            list.add(new CompletionChoice((JsonObject) x));
        });
        return list;
    }

    public static class CompletionChoice extends SimpleJsonifiableEntity {
        public CompletionChoice(JsonObject jsonObject) {
            super(jsonObject);
        }

        public String getText() {
            return readString("text");
        }

        public Integer getIndex() {
            return readInteger("index");
        }

        public Object getLogProbs() {
            return readValue("logprobs");
        }

        public String getFinishReason() {
            return readString("finish_reason");
        }
    }
}
