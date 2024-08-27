package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.core.filter.OpenAIContentFilterPromptResults;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface OpenAIChatGptResponseChoice extends UnmodifiableJsonifiableEntity {
    static OpenAIChatGptResponseChoice wrap(JsonObject json) {
        return new OpenAIChatGptResponseChoiceImpl(json);
    }

    default Integer getIndex() {
        return readInteger("index");
    }

    default String getFinishReason() {
        return readString("finish_reason");
    }

    default OpenAIContentFilterPromptResults getContentFilterPromptResults() {
        JsonObject contentFilterResults = readJsonObject("content_filter_results");
        if (contentFilterResults == null) {
            return null;
        }
        return OpenAIContentFilterPromptResults.wrap(contentFilterResults);
    }

    // logprobs

    default AssistantMessage getMessage() {
        JsonObject x = readJsonObject("message");
        return new AssistantMessage(x);
    }
}
