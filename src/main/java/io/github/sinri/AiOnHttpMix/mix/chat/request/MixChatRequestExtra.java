package io.github.sinri.AiOnHttpMix.mix.chat.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface MixChatRequestExtra extends JsonifiableEntity<MixChatRequestExtra> {
    static MixChatRequestExtra create() {
        return new MixChatRequestExtraImpl();
    }

    static MixChatRequestExtra wrap(JsonObject jsonObject) {
        return new MixChatRequestExtraImpl(jsonObject);
    }

    default Float getTemperature() {
        return readFloat("temperature");
    }

    default MixChatRequestExtra setTemperature(float temperature) {
        return this.write("temperature", temperature);
    }

    default JsonObject getResponseFormat() {
        return readJsonObject("response_format");
    }

    /**
     * Let the response format of this chat model request be as the ordered, plain
     * text, a json object, even follow the json schema.
     * Its effect differs with different chat model.
     */
    default MixChatRequestExtra setResponseFormat(JsonObject responseFormat) {
        return this.write("response_format", responseFormat);
    }
}
