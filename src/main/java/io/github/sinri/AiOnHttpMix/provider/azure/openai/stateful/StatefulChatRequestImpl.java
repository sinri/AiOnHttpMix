package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.1
 */
class StatefulChatRequestImpl extends JsonifiableEntityImpl<StatefulChatRequest> implements StatefulChatRequest {
    public StatefulChatRequestImpl() {
        super();
    }

    public StatefulChatRequestImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public StatefulChatRequest getImplementation() {
        return this;
    }
}
