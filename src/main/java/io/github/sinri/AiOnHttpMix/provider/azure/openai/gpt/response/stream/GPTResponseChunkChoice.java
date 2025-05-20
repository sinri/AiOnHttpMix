package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface GPTResponseChunkChoice extends UnmodifiableJsonifiableEntity {
    static GPTResponseChunkChoice wrap(JsonObject jsonObject) {
        return new GPTResponseChunkChoiceImpl(jsonObject);
    }

    Object get = null;

    default JsonObject getContentFilterResults() {
        return readJsonObject("content_filter_results");
    }

    default String getFinishReason() {
        return readString("finish_reason");
    }

    default Integer getIndex() {
        return readInteger("index");
    }

    default GPTResponseChunkChoiceDelta getDelta() {
        JsonObject x = readJsonObject("delta");
        if (x == null) {
            x = new JsonObject();
        }
        return GPTResponseChunkChoiceDelta.wrap(x);
    }

    // logprobs is not supported yet
}
