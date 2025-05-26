package io.github.sinri.AiOnHttpMix.provider.azure.bing.search.v7;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class BingSearchParametersImpl extends JsonifiableEntityImpl<BingSearchParameters> implements BingSearchParameters {

    public BingSearchParametersImpl() {
        super();
    }

    public BingSearchParametersImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public BingSearchParameters getImplementation() {
        return this;
    }
}
