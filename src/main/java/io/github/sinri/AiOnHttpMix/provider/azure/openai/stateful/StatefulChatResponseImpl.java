package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.1
 */
class StatefulChatResponseImpl extends UnmodifiableJsonifiableEntityImpl implements StatefulChatResponse {
    public StatefulChatResponseImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
