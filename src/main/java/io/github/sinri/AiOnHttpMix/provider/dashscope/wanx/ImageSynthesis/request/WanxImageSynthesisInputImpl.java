package io.github.sinri.AiOnHttpMix.provider.dashscope.wanx.ImageSynthesis.request;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class WanxImageSynthesisInputImpl implements WanxImageSynthesisInput {
    private JsonObject jsonObject = new JsonObject();

    @Override
    public @Nonnull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @Nonnull WanxImageSynthesisInput reloadDataFromJsonObject(@Nonnull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Nonnull
    @Override
    public WanxImageSynthesisInput getImplementation() {
        return this;
    }
}
