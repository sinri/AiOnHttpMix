package io.github.sinri.AiOnHttpMix.volces.v3.visual;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.2.6
 */
class VolcesVisualChatMessageContentImageMetaImpl implements VolcesVisualChatMessageContentImageMeta {
    private JsonObject jsonObject;

    public VolcesVisualChatMessageContentImageMetaImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public @NotNull VolcesVisualChatMessageContentImageMeta reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
