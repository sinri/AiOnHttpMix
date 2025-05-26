package io.github.sinri.AiOnHttpMix.provider.azure.bing.search.v7;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class WebPageImpl extends UnmodifiableJsonifiableEntityImpl implements BingSearchResponse.WebPage {
    public WebPageImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
