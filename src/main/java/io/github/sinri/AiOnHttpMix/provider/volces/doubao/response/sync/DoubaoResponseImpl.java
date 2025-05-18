package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;


class DoubaoResponseImpl extends JsonifiableEntityImpl<DoubaoResponse> implements DoubaoResponse {

    public DoubaoResponseImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    public DoubaoResponseImpl() {
        super();
    }

    @Nonnull
    @Override
    public DoubaoResponse getImplementation() {
        return this;
    }
}
