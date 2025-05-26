package io.github.sinri.AiOnHttpMix.provider.azure.bing.search.v7;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class QueryContextImpl extends UnmodifiableJsonifiableEntityImpl implements BingSearchResponse.QueryContext {
    public QueryContextImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
