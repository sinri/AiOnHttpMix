package io.github.sinri.AiOnHttpMix.provider.volces.doubao.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ThinkingOptionsImpl extends JsonifiableEntityImpl<ThinkingOptions> implements ThinkingOptions {
    public ThinkingOptionsImpl() {
        super();
    }

    public ThinkingOptionsImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public ThinkingOptions getImplementation() {
        return this;
    }
} 