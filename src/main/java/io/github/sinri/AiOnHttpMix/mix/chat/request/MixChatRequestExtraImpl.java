package io.github.sinri.AiOnHttpMix.mix.chat.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class MixChatRequestExtraImpl extends JsonifiableEntityImpl<MixChatRequestExtra> implements MixChatRequestExtra {
    public MixChatRequestExtraImpl() {
        super();
    }

    public MixChatRequestExtraImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public MixChatRequestExtra getImplementation() {
        return this;
    }
}
