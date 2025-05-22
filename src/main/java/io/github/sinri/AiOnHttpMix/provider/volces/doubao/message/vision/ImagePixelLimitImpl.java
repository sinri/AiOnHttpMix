package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ImagePixelLimitImpl extends io.github.sinri.keel.core.json.JsonifiableEntityImpl<ContentImageUrl.ImagePixelLimit> implements ContentImageUrl.ImagePixelLimit {
    public ImagePixelLimitImpl() {
        super();
    }

    public ImagePixelLimitImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    @Nonnull
    public ContentImageUrl.ImagePixelLimit getImplementation() {
        return this;
    }
}
