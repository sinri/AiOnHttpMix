package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ContentVideoUrlImpl extends JsonifiableEntityImpl<ContentVideoUrl> implements ContentVideoUrl {
    public ContentVideoUrlImpl() {
        super();
    }

    public ContentVideoUrlImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public ContentVideoUrl getImplementation() {
        return this;
    }
}
