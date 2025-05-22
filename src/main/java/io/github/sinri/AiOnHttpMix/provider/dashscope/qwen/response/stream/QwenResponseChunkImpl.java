package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class QwenResponseChunkImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseChunk {
    public QwenResponseChunkImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
