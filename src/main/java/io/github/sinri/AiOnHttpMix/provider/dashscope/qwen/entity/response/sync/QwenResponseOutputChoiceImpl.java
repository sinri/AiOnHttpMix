package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

/**
 * @since 2.0.0
 */
class QwenResponseOutputChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseOutputChoice {
    public QwenResponseOutputChoiceImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
