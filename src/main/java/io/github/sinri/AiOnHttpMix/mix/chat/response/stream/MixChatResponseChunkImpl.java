package io.github.sinri.AiOnHttpMix.mix.chat.response.stream;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class MixChatResponseChunkImpl extends JsonifiableEntityImpl<MixChatResponseChunk> implements MixChatResponseChunk {
    public MixChatResponseChunkImpl() {
        super();
    }

    public MixChatResponseChunkImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public MixChatResponseChunk getImplementation() {
        return this;
    }
}
