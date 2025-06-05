package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful.content.StatefulChatItemContentOutputText;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @since 2.0.1
 */
public interface StatefulChatResponseOutputItem extends UnmodifiableJsonifiableEntity {
    static StatefulChatResponseOutputItem wrap(JsonObject jsonObject) {
        return new StatefulChatResponseOutputItemImpl(jsonObject);
    }

    default String getId() {
        return this.readString("id");
    }

    default String getType() {
        return this.readString("type");
    }

    default String getStatus() {
        return this.readString("status");
    }

    default List<StatefulChatItemContentOutputText> getContent() {
        var a = this.readJsonObjectArray("content");
        if (a == null) {
            return List.of();
        }
        return a.stream().map(StatefulChatItemContentOutputText::wrap).toList();
    }

    default String getRole() {
        return this.readString("role");
    }
}
