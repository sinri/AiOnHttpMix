package io.github.sinri.AiOnHttpMix.volces.v3.visual;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.2.6
 */
class VolcesVisualChatMessageContentImpl implements VolcesVisualChatMessageContent {
    private JsonObject jsonObject;

    public VolcesVisualChatMessageContentImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public @NotNull VolcesVisualChatMessageContent reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

}
