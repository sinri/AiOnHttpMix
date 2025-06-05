package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful.content;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

/**
 * OpenAI.ItemContent.
 *
 * @see <a
 *         href="https://learn.microsoft.com/en-us/azure/ai-services/openai/reference-preview-latest#discriminator-for-openaiitemcontent">Discriminator
 *         for OpenAI.ItemContent</a>
 * @since 2.0.1
 */
public interface StatefulChatItemContent extends UnmodifiableJsonifiableEntity {
    static StatefulChatItemContent wrap(JsonObject jsonObject) {
        return new StatefulChatItemContentImpl(jsonObject);
    }

    /**
     * @return This component uses the property type to discriminate between different types.
     */
    default String getType() {
        return this.readString("type");
    }


}
