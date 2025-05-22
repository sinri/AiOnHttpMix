package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync;

import javax.annotation.Nonnull;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
/**
 * @since 2.0.0
 */
class QwenResponseOutputSearchInfoImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseOutputSearchInfo {
    public QwenResponseOutputSearchInfoImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class SearchResultImpl extends UnmodifiableJsonifiableEntityImpl implements SearchResult {

        public SearchResultImpl(@Nonnull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
