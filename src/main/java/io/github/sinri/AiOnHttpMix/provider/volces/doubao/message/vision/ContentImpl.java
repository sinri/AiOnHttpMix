package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ContentImpl extends JsonifiableEntityImpl<Content> implements Content {
    public ContentImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    public ContentImpl() {
        super();
    }

    @Nonnull
    @Override
    public Content getImplementation() {
        return this;
    }
}
