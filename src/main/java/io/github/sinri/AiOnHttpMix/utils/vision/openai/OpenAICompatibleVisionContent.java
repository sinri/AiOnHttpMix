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
     * @return 图片消息的内容部分。
     */
    P getImageUrl();
    //    default P getImageUrl() {
    //        JsonObject x = readJsonObject("image_url");
    //        Objects.requireNonNull(x);
    //        return OpenAICompatibleVisionContentForImageUrl.wrap(x);
    //    }

    /**
     * For type {@code image_url}.
     */
    default <E> C setImageUrl(P imageUrl) {
        setType("image_url");
        return write("image_url", imageUrl.toJsonObject());
    }
}
