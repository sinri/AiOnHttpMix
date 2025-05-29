package io.github.sinri.AiOnHttpMix.mix.chat.response.stream;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class MixChatResponseChunkChoiceImpl extends JsonifiableEntityImpl<MixChatResponseChunkChoice> implements MixChatResponseChunkChoice {
    public MixChatResponseChunkChoiceImpl() {
        super();
    }

    public MixChatResponseChunkChoiceImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public MixChatResponseChunkChoice getImplementation() {
        return this;
    }
}
