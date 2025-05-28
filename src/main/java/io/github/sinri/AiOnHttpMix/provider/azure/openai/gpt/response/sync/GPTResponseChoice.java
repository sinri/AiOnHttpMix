package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter.OpenAIContentFilterChoiceResults;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInResponse;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface GPTResponseChoice extends UnmodifiableJsonifiableEntity {
    static GPTResponseChoice wrap(JsonObject jsonObject) {
        return new GPTResponseChoiceImpl(jsonObject);
    }

    default OpenAIContentFilterChoiceResults getContentFilterResults() {
        var x = readJsonObject("content_filter_results");
        if (x == null) x = new JsonObject();
        return OpenAIContentFilterChoiceResults.wrap(x);
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
