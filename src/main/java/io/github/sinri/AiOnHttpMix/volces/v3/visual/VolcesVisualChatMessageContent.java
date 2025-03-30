package io.github.sinri.AiOnHttpMix.volces.v3.visual;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.2.6
 */
public interface VolcesVisualChatMessageContent extends JsonifiableEntity<VolcesVisualChatMessageContent> {
    static VolcesVisualChatMessageContent wrap(JsonObject jsonObject) {
        return new VolcesVisualChatMessageContentImpl(jsonObject);
    }

    static VolcesVisualChatMessageContent create() {
        return new VolcesVisualChatMessageContentImpl(new JsonObject());
    }

    /**
     * 传入信息为文本信息设置为text。
     * 传入信息为图片信息设置为image_url。
     *
     * @return 传入的信息类型。text 或 image_url。
     */
    default String getType() {
        return readString("type");
    }

    @Nullable
    default String getText() {
        return readString("text");
    }

    default VolcesVisualChatMessageContent setText(String text) {
        toJsonObject().put("type", "text").put("text", text);
        return this;
    }

    @Nullable
    default VolcesVisualChatMessageContentImageMeta getImageMeta() {
        var x = readJsonObject("image_url");
        if (x == null) return null;
        return VolcesVisualChatMessageContentImageMeta.wrap(x);
    }

    default VolcesVisualChatMessageContent setImageMeta(VolcesVisualChatMessageContentImageMeta imageMeta) {
        toJsonObject().put("type", "image_url")
                      .put("image_url", imageMeta.toJsonObject());
        return this;
    }

}
