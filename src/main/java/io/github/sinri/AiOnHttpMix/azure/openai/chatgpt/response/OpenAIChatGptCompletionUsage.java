package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface OpenAIChatGptCompletionUsage extends UnmodifiableJsonifiableEntity {
    static OpenAIChatGptCompletionUsage wrap(JsonObject jsonObject) {
        return new OpenAIChatGptCompletionUsageImpl(jsonObject);
    }

    /**
     * @return Number of tokens in the prompt.
     */
    default Integer getPromptTokens() {
        return readInteger("prompt_tokens");
    }

    /**
     * @return Number of tokens in the generated completion.
     */
    default Integer getCompletionTokens() {
        return readInteger("completion_tokens");
    }

    /**
     * @return Total number of tokens used in the request (prompt + completion).
     */
    default Integer getTotalTokens() {
        return readInteger("total_tokens");
    }

}
