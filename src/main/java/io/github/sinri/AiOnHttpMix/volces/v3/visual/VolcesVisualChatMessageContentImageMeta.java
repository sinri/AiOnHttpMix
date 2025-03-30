package io.github.sinri.AiOnHttpMix.volces.v3.visual;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.2.6
 */
public interface VolcesVisualChatMessageContentImageMeta extends JsonifiableEntity<VolcesVisualChatMessageContentImageMeta> {
    static VolcesVisualChatMessageContentImageMeta wrap(JsonObject jsonObject) {
        return new VolcesVisualChatMessageContentImageMetaImpl(jsonObject);
    }

    static VolcesVisualChatMessageContentImageMeta create() {
        return new VolcesVisualChatMessageContentImageMetaImpl(new JsonObject());
    }

    default VolcesVisualChatMessageContentImageMeta setImage(String urlOrBase64Encoded) {
        this.toJsonObject().put("url", urlOrBase64Encoded);
        return this;
    }

    /**
     * 如果传入Base64编码：请遵循格式{@code data:image/FORMAT;base64,ENCODED}，FORMAT：图片的格式，ENCODED：图片的Base64编码。
     *
     * @return 图片链接或图片的Base64编码
     */
    default String getUrlOrBase64Encoded() {
        return readString("url");
    }

    @Nullable
    default String getUrl() {
        String urlOrBase64Encoded = getUrlOrBase64Encoded();
        if (urlOrBase64Encoded.startsWith("http")) {
            return urlOrBase64Encoded;
        }
        return null;
    }

    @Nullable
    default String getBase64Encoded() {
        String urlOrBase64Encoded = getUrlOrBase64Encoded();
        if (urlOrBase64Encoded.startsWith("data:image/")) {
            return urlOrBase64Encoded;
        }
        return null;
    }

    /**
     * high：高细节模式，适用于需要理解图像细节信息的场景，如对图像的多个局部信息/特征提取、复杂/丰富细节的图像理解等场景，理解更全面。
     * low：低细节模式，适用于简单的图像分类/识别、整体内容理解/描述等场景，理解更快速。
     * auto：默认模式，不同模型选择的模式略有不同，具体请参见理解图像的深度控制。
     *
     * @return 手动设置图片的质量，取值范围high、low、auto
     * @see <a href="https://www.volcengine.com/docs/82379/1362931#bf4d9224">理解图像的深度控制</a>
     */
    default String getDetail() {
        return readString("detail");
    }

    /**
     * @param detail 手动设置图片的质量，取值范围high、low、auto
     */
    default VolcesVisualChatMessageContentImageMeta setDetail(String detail) {
        toJsonObject().put("detail", detail);
        return this;
    }
}
