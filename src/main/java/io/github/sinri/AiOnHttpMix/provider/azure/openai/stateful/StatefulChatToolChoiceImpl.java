package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.1
 */
class StatefulChatToolChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements StatefulChatToolChoice {
    public StatefulChatToolChoiceImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
