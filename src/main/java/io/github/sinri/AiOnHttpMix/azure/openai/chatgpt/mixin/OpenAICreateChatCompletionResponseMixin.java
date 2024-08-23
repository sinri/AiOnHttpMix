package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter.ContentFilterPromptResultsImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface OpenAICreateChatCompletionResponseMixin extends UnmodifiableJsonifiableEntity {
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
    CompletionUsage getUsage();

    /**
     * @return Can be used in conjunction with the seed request parameter to understand when backend changes have been made that might impact determinism.
     */
    default String getSystemFingerprint() {
        return readString("system_fingerprint");
    }

    /**
     * @return Content filtering results for zero or more prompts in the request. In a streaming request, results for different prompts may arrive at different times or in different orders.
     */
    PromptFilterResults getPromptFilterResults();

    List<Choice> getChoices();

    interface CompletionUsage extends UnmodifiableJsonifiableEntity {
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

    interface Choice extends UnmodifiableJsonifiableEntity {
        // todo

        default Integer getIndex() {
            return readInteger("index");
        }

        default String getFinishReason() {
            return readString("finish_reason");
        }

        default ContentFilterPromptResults getContentFilterPromptResults() {
            JsonObject contentFilterResults = readJsonObject("content_filter_results");
            if (contentFilterResults == null) {
                return null;
            }
            return new ContentFilterPromptResultsImpl(contentFilterResults);
        }

        // logprobs

        default AssistantMessage getMessage() {
            JsonObject x = readJsonObject("message");
            return new AssistantMessage(x);
        }
    }
}