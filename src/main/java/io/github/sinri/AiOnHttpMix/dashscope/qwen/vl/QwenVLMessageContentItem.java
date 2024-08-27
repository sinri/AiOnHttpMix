package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface QwenVLMessageContentItem extends JsonifiableEntity<QwenVLMessageContentItem> {
    static QwenVLMessageContentItem create() {
        return new QwenVLMessageContentItemImpl();
    }

    static QwenVLMessageContentItem wrap(JsonObject json) {
        return new QwenVLMessageContentItemImpl(json);
    }

    /**
     * 图像文件限制与支持格式详见文档上方模型介绍部分。
     *
     * @param image 本次输入的图像内容的 url 链接
     */
    default QwenVLMessageContentItem setImage(String image) {
        toJsonObject().put("image", image);
        return this;
    }

    /**
     * 支持 utf-8 编码的中文、英文输入。
     *
     * @param text 本次输入的文本内容
     */
    default QwenVLMessageContentItem setText(String text) {
        toJsonObject().put("text", text);
        return this;
    }

    /**
     * @return 本次输入的图像内容的 url 链接
     */
    default String getImage() {
        return readString("image");
    }

    /**
     * @return 本次输入的文本内容
     */
    default String getText() {
        return readString("text");
    }
}
