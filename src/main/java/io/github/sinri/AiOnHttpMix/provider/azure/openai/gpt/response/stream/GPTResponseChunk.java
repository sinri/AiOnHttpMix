package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface GPTResponseChunk extends UnmodifiableJsonifiableEntity {
    static GPTResponseChunk wrap(JsonObject jsonObject) {
        return new GPTResponseChunkImpl(jsonObject);
    }

    default Integer getCreated() {
        return this.readInteger("created");
    }

    default String getId() {
        return this.readString("id");
    }

    default String getModel() {
        return this.readString("model");
    }

    default String getObject() {
        return this.readString("object");
    }

    default List<JsonObject> getPromptFilterResults() {
        return readJsonObjectArray("prompt_filter_results");
    }

    default List<GPTResponseChunkChoice> getChoices() {
        List<JsonObject> a = readJsonObjectArray("choices");
        if (a == null) return List.of();
        return a.stream().map(GPTResponseChunkChoice::wrap).toList();
    }

}
