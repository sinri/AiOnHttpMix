package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

/**
 * o-series models only.
 *
 * @since 2.0.1
 */
public interface StatefulChatReasoning extends UnmodifiableJsonifiableEntity {
    static StatefulChatReasoning wrap(JsonObject jsonObject) {
        return new StatefulChatReasoningImpl(jsonObject);
    }

    /**
     * Constrains effort on reasoning for reasoning models.
     * Currently supported values are low, medium, and high.
     * Reducing reasoning effort can result in faster responses and fewer tokens used on reasoning in a response.
     */
    default String getEffort() {
        return this.readString("effort");
    }

    /**
     * A summary of the reasoning performed by the model. This can be
     * useful for debugging and understanding the model's reasoning process.
     * One of auto, concise, or detailed.
     *
     * @return Possible values: auto, concise, detailed
     */
    default String getSummary() {
        return this.readString("summary");
    }
}
