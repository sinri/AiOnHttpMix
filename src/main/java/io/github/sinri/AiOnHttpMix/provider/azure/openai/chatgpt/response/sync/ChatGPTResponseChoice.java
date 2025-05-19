package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.response.sync;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.message.ChatGPTMessageInResponse;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface ChatGPTResponseChoice extends UnmodifiableJsonifiableEntity {
    static ChatGPTResponseChoice wrap(JsonObject jsonObject) {
        return new ChatGPTResponseChoiceImpl(jsonObject);
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

    default ChatGPTMessageInResponse getMessage() {
        return ChatGPTMessageInResponse.wrap(readJsonObject("message"));
    }
}
