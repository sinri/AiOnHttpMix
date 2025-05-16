package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class QwenResponseOutputSearchInfoImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseOutputSearchInfo {
    public QwenResponseOutputSearchInfoImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class SearchResultImpl extends UnmodifiableJsonifiableEntityImpl implements SearchResult {

        public SearchResultImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
