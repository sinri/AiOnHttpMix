package io.github.sinri.AiOnHttpMix.provider.volces.doubao.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class DoubaoRequestImpl extends JsonifiableEntityImpl<DoubaoRequest> implements DoubaoRequest {
    public DoubaoRequestImpl() {
        super();
    }

    public DoubaoRequestImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public DoubaoRequest getImplementation() {
        return this;
    }
}
