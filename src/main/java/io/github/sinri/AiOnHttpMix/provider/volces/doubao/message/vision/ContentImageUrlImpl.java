package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

// 包私有实现类
class ContentImageUrlImpl extends io.github.sinri.keel.core.json.JsonifiableEntityImpl<ContentImageUrl> implements ContentImageUrl {
    public ContentImageUrlImpl() {
        super();
    }

    public ContentImageUrlImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    @Nonnull
    public ContentImageUrl getImplementation() {
        return this;
    }
}
