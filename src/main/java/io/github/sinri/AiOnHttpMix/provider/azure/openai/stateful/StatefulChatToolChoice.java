package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

/**
 * @since 2.0.1
 */
public interface StatefulChatToolChoice extends UnmodifiableJsonifiableEntity {
    static StatefulChatToolChoice wrap(JsonObject jsonObject) {
        return new StatefulChatToolChoiceImpl(jsonObject);
    }

    /**
     * Indicates that the model should use a built-in tool to generate a response.
     */
    default String getType() {
        return this.readString("type");
    }
}
