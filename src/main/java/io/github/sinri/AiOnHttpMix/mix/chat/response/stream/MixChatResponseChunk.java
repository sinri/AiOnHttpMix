package io.github.sinri.AiOnHttpMix.mix.chat.response.stream;

import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface MixChatResponseChunk extends JsonifiableEntity<MixChatResponseChunk> {
    default Integer getCreated(){
        return readInteger("created");
    }

    default String getId() {
        return this.readString("id");
    }

    default String getModel() {
        return this.readString("model");
    }

    default String getObject() {
        return this.readString("object");
    }


}
