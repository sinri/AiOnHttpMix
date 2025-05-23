package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class MixChatResponseImpl extends JsonifiableEntityImpl<MixChatResponse> implements MixChatResponse {
    public MixChatResponseImpl() {
        super();
    }

    public MixChatResponseImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public MixChatResponse getImplementation() {
        return this;
    }

    @Override
    public MixChatMessage getMessage() {
        JsonObject message = readJsonObject("message");
        return MixChatMessage.wrap(message);
    }
}
