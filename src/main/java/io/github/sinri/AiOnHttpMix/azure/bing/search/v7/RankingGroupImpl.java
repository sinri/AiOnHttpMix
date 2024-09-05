package io.github.sinri.AiOnHttpMix.azure.bing.search.v7;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class RankingGroupImpl extends UnmodifiableJsonifiableEntityImpl implements BingSearchResponse.RankingGroup {
    public RankingGroupImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
