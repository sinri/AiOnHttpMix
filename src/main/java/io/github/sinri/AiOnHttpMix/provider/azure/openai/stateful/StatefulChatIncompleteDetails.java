package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

/**
 * @since 2.0.1
 */
public interface StatefulChatIncompleteDetails extends UnmodifiableJsonifiableEntity {
    static StatefulChatIncompleteDetails wrap(JsonObject jsonObject) {
        return new StatefulChatIncompleteDetailsImpl(jsonObject);
    }

    /**
     * The reason why the response is incomplete.
     *
     * @return Possible values: max_output_tokens, content_filter
     */
    default String getReason() {
        return this.readString("reason");
    }
}
