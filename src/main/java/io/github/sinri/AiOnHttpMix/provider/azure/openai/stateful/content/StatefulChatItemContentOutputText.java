package io.github.sinri.AiOnHttpMix.provider.azure.openai.stateful.content;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Its type is {@code output_text}.
 *
 * @since 2.0.1
 */
public interface StatefulChatItemContentOutputText extends StatefulChatItemContent {
    String TYPE = "output_text";

    static StatefulChatItemContentOutputText wrap(JsonObject jsonObject) {
        return new StatefulChatItemContentImpl(jsonObject);
    }

    /**
     * @return The text output from the model.
     */
    default String getText() {
        return this.readString("text");
    }

    /**
     * @return The annotations of the text output.
     */
    default JsonArray getAnnotations() {
        return this.readJsonArray("annotations");
    }
}
