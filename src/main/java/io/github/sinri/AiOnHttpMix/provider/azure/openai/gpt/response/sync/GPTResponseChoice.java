package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInResponse;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface GPTResponseChoice extends UnmodifiableJsonifiableEntity {
    static GPTResponseChoice wrap(JsonObject jsonObject) {
        return new GPTResponseChoiceImpl(jsonObject);
    }

    default JsonObject getContentFilterResults() {
        return readJsonObject("content_filter_results");
    }

    default String getFinishReason() {
        return readString("finish_reason");
    }

    default Integer getIndex() {
        return readInteger("index");
    }

    default JsonObject getLogprobs() {
        return readJsonObject("logprobs");
    }

    default GPTMessageInResponse getMessage() {
        return GPTMessageInResponse.wrap(readJsonObject("message"));
    }
}
