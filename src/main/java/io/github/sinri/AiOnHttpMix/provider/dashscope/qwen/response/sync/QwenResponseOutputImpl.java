package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync;

import javax.annotation.Nonnull;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

/**
 * @since 2.0.0
 */
class QwenResponseOutputImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseOutput {
    public QwenResponseOutputImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
