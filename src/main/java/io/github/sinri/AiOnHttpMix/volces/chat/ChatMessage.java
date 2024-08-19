package io.github.sinri.AiOnHttpMix.volces.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public class ChatMessage extends SimpleJsonifiableEntity {
    public ChatMessage() {
        super();
    }

    public ChatMessage(JsonObject jsonObject) {
        super(jsonObject);
    }

    public ChatRole getRole() {
        return ChatRole.valueOf(this.jsonObject.getString("role"));
    }

    public ChatMessage setRole(ChatRole role) {
        this.jsonObject.put("role", role.name());
        return this;
    }

    /**
     * @param content 消息的内容
     */
    public ChatMessage setContent(String content) {
        this.jsonObject.put("content", content);
        return this;
    }

    /**
     * Only for role user.
     *
     * @param content 当模型支持多模态时，可以为 list，list 的元素可以为 text 或 image_url
     */
    public ChatMessage setContent(MixedContent content) {
        this.jsonObject.put("content", content.toJsonObject());
        return this;
    }

    abstract public static class MixedContent extends SimpleJsonifiableEntity {
        public MixedContent(JsonObject jsonObject) {
            super(jsonObject);
        }

        public String getType() {
            return readString("type");
        }
    }

    public static class MixedContentAsText extends MixedContent {
        public MixedContentAsText(String text) {
            super(new JsonObject().put("type", "text").put("text", text));
        }

        public String getText() {
            return readString("text");
        }
    }

    public static class MixedContentAsImageUrl extends MixedContent {
        public MixedContentAsImageUrl(String url) {
            super(new JsonObject().put("type", "image_url")
                    .put("image_url", new JsonObject()
                            .put("url", url)));
        }

        public String getText() {
            return readString("text");
        }
    }


}
