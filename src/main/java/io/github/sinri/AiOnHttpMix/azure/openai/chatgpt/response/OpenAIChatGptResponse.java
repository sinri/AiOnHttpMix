package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.AiOnHttpMix.azure.openai.core.filter.OpenAIPromptFilterResults;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface OpenAIChatGptResponse extends UnmodifiableJsonifiableEntity {
    static OpenAIChatGptResponse wrap(JsonObject response) {
        return new OpenAIChatCompletionResponseImpl(response);
    }

    /**
     * @return A unique identifier for the chat completion.
     */
    default String getId() {
        return readString("id");
    }

    /**
     * @return The object type: `chat.completion`.
     */
    default String getObject() {
        return readString("object");
    }

    /**
     * @return The Unix timestamp (in seconds) of when the chat completion was created.
     */
    default Integer getCreated() {
        return readInteger("created");
    }

    /**
     * @return The model used for the chat completion.
     */
    default String getModel() {
        return readString("model");
    }

    /**
     * @return Usage statistics for the completion request.
     */
    OpenAIChatGptCompletionUsage getUsage();

    /**
     * @return Can be used in conjunction with the seed request parameter to understand when backend changes have been made that might impact determinism.
     */
    default String getSystemFingerprint() {
        return readString("system_fingerprint");
    }

    /**
     * @return Content filtering results for zero or more prompts in the request. In a streaming request, results for different prompts may arrive at different times or in different orders.
     */
    OpenAIPromptFilterResults getPromptFilterResults();

    List<OpenAIChatGptResponseChoice> getChoices();

}