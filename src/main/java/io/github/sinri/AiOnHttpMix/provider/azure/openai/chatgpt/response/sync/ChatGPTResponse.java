package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface ChatGPTResponse extends UnmodifiableJsonifiableEntity {
    static ChatGPTResponse wrap(JsonObject jsonObject) {
        return new ChatGPTResponseImpl(jsonObject);
    }

    /**
     * @return A unique identifier for the completion.
     */
    default String getId() {
        return readString("id");
    }

    /**
     * @return The Unix timestamp (in seconds) of when the completion was created.
     */
    default Integer getCreated() {
        return readInteger("created");
    }

    /**
     * @return The object type, which is always "text_completion"
     *         Possible values: text_completion
     */
    default String getObject() {
        return readString("object");
    }

    /**
     * @return The model used for completion.
     */
    default String getModel() {
        return readString("model");
    }

    /**
     * This fingerprint represents the backend configuration that the model runs
     * with.
     * <p>
     * Can be used in conjunction with the seed request parameter to understand when
     * backend changes have been made that might impact determinism.
     */
    default String getSystemFingerprint() {
        return readString("system_fingerprint");
    }

    default List<ChatGPTResponseChoice> getChoices() {
        List<JsonObject> array = readJsonObjectArray("choices");
        if (array == null) {
            return List.of();
        }
        return array.stream().map(ChatGPTResponseChoice::wrap).toList();
    }

    /**
     * Usage statistics for the completion request.
     */
    default JsonObject getUsage() {
        return readJsonObject("usage");
    }

    /**
     * Content filtering results for zero or more prompts in the request. In a
     * streaming request, results for different prompts may arrive at different
     * times or in different orders.
     */
    default JsonObject getPromptFilterResults() {
        return readJsonObject("prompt_filter_results");
    }

}
