package io.github.sinri.AiOnHttpMix.utils.vision.openai;

import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface OpenAICompatibleVisionContent<C, P extends OpenAICompatibleVisionContentForImageUrl<P>> extends JsonifiableEntity<C> {
    default String getType() {
        return readString("type");
    }

    default C setType(String type) {
        return write("type", type);
    }

    default String getText() {
        return readString("text");
    }

    /**
     * For type {@code text}.
     */
    default C setText(String text) {
        setType("text");
        return write("text", text);
    }


    /**
     * Sample code:
     * {@code return P.wrap(Objects.requireNonNull(readJsonObject("image_url")));}
     *
     * @return 图片消息的内容部分。
     */
    P getImageUrl();

    /**
     * For type {@code image_url}.
     */
    default <E> C setImageUrl(P imageUrl) {
        setType("image_url");
        return write("image_url", imageUrl.toJsonObject());
    }
}
