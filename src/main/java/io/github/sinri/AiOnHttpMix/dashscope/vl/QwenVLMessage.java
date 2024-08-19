package io.github.sinri.AiOnHttpMix.dashscope.vl;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QwenVLMessage extends SimpleJsonifiableEntity {
    public QwenVLMessage(JsonObject jsonObject) {
        super(jsonObject);
    }

    public QwenVLMessage() {
        super();
        this.jsonObject.put("content", new JsonArray());
    }

    public QwenVLRole getRole() {
        return QwenVLRole.valueOf(readString("role"));
    }

    /**
     * @param role 信息来源: user
     */
    public QwenVLMessage setRole(@NotNull QwenVLRole role) {
        this.jsonObject.put("role", role.name());
        return this;
    }

    /**
     * 本次输入的内容列表的其中一项
     */
    public QwenVLMessage addContentItem(ContentItem contentItem) {
        this.jsonObject.getJsonArray("content")
                .add(contentItem.toJsonObject());
        return this;
    }

    public List<ContentItem> getContentItems() {
        List<ContentItem> contentItems = new ArrayList<>();
        for (JsonObject content : readJsonObjectArray("content")) {
            contentItems.add(new ContentItem(content));
        }
        return contentItems;
    }

    public enum ContentItemType {
        /**
         * 本次输入的文本内容；支持 utf-8 编码的中文、英文输入。
         */
        text,
        /**
         * 本次输入的图像内容的 url 链接；图像格式目前支持：bmp, jpg, jpeg, png 和 tiff。
         */
        image
    }

    public static class ContentItem extends SimpleJsonifiableEntity {
        public ContentItem(JsonObject jsonObject) {
            super(jsonObject);
        }

        public ContentItem(@NotNull ContentItemType type, @NotNull String value) {
            this(new JsonObject().put(type.name(), value));
        }

        public ContentItemType getType() {
            return ContentItemType.valueOf(readString("type"));
        }

        public String getValue() {
            return readString(getType().name());
        }
    }
}
