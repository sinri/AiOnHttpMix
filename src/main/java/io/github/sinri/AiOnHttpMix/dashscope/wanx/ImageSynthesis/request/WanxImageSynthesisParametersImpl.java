package io.github.sinri.AiOnHttpMix.dashscope.wanx.ImageSynthesis.request;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class WanxImageSynthesisParametersImpl implements WanxImageSynthesisParameters {
    private JsonObject jsonObject = new JsonObject();

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull WanxImageSynthesisParameters reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
