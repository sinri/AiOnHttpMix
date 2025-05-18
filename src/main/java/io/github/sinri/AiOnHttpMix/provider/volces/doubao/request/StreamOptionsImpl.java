package io.github.sinri.AiOnHttpMix.provider.volces.doubao.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class StreamOptionsImpl extends JsonifiableEntityImpl<StreamOptions> implements StreamOptions {
    public StreamOptionsImpl() {
        super();
    }

    public StreamOptionsImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public StreamOptions getImplementation() {
        return this;
    }
} 