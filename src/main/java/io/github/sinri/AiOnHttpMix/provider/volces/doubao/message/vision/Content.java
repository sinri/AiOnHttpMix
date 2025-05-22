package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public interface Content extends JsonifiableEntity<Content> {
    static Content create() {
        return new ContentImpl();
    }

    static Content wrap(JsonObject jsonObject) {
        return new ContentImpl(jsonObject);
    }

    default String getType() {
        return readString("type");
    }

    default Content setType(String type) {
        return write("type", type);
    }

    default String getText() {
        return readString("text");
    }

    /**
     * For type {@code text}.
     */
    default Content setText(String text) {
        return write("text", text);
    }

    /**
     * @return 图片消息的内容部分。
     */
    default ContentImageUrl getImageUrl() {
        JsonObject x = readJsonObject("image_url");
        Objects.requireNonNull(x);
        return ContentImageUrl.wrap(x);
    }

    /**
     * For type {@code image_url}.
     */
    default Content setImageUrl(ContentImageUrl imageUrl) {
        return write("image_url", imageUrl.toJsonObject());
    }
}
