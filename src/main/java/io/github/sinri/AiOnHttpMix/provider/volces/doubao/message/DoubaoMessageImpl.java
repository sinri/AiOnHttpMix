package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class DoubaoMessageImpl extends JsonifiableEntityImpl<DoubaoMessage>
        implements DoubaoMessageInChatRequest,
        DoubaoMessageInResponse,
        DoubaoMessageInVisionRequest {
    public DoubaoMessageImpl() {
        super();
    }

    public DoubaoMessageImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public DoubaoMessage getImplementation() {
        return this;
    }
}
