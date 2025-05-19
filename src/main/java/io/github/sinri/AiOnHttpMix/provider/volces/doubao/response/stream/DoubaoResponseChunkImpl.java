package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class DoubaoResponseChunkImpl extends UnmodifiableJsonifiableEntityImpl implements DoubaoResponseChunk {
    public DoubaoResponseChunkImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
