package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.1
 */
class StatefulChatResponseOutputItemImpl extends UnmodifiableJsonifiableEntityImpl implements StatefulChatResponseOutputItem {
    public StatefulChatResponseOutputItemImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
