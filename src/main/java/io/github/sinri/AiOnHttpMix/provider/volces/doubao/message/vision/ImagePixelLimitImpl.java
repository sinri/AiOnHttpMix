package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision;

import io.vertx.core.json.JsonObject;

class ImagePixelLimitImpl extends io.github.sinri.keel.core.json.JsonifiableEntityImpl<ContentImageUrl.ImagePixelLimit> implements ContentImageUrl.ImagePixelLimit {
    public ImagePixelLimitImpl() {
        super();
    }

    public ImagePixelLimitImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public ContentImageUrl.ImagePixelLimit getImplementation() {
        return this;
    }
}
