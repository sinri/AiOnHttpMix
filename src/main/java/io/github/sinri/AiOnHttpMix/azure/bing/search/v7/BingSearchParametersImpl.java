package io.github.sinri.AiOnHttpMix.azure.bing.search.v7;

import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class BingSearchParametersImpl implements BingSearchParameters {
    private JsonObject jsonObject;

    public BingSearchParametersImpl() {
        this.jsonObject = new JsonObject();
    }

    public BingSearchParametersImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull BingSearchParameters reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
