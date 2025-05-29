package io.github.sinri.AiOnHttpMix.mix.chat.response;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class MixChatResponseImpl extends JsonifiableEntityImpl<MixChatResponse> implements MixChatResponse {
    public final static String KEY_MESSAGE = "message";

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
        JsonObject message = readJsonObject(KEY_MESSAGE);
        return MixChatMessage.wrap(message);
    }

    @Override
    public MixChatResponse setMessage(MixChatMessage message) {
        return write(KEY_MESSAGE, message.toJsonObject());
    }
}
