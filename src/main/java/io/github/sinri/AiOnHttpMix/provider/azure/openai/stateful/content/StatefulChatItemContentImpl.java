package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful.content;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.1
 */
class StatefulChatItemContentImpl extends UnmodifiableJsonifiableEntityImpl
        implements StatefulChatItemContent, StatefulChatItemContentOutputText {
    public StatefulChatItemContentImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
