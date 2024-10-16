package io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class WanxImageSynthesisInputImpl implements WanxImageSynthesisInput {
    private JsonObject jsonObject = new JsonObject();

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull WanxImageSynthesisInput reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
