package io.github.sinri.AiOnHttpMix.azure.bing.search.v7;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class RankingItemImpl extends UnmodifiableJsonifiableEntityImpl implements BingSearchResponse.RankingItem {
    public RankingItemImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
