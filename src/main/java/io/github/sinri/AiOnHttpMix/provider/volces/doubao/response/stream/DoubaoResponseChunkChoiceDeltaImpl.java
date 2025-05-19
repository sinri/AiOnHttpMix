package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class DoubaoResponseChunkChoiceDeltaImpl extends UnmodifiableJsonifiableEntityImpl implements DoubaoResponseChunkChoiceDelta {
    public DoubaoResponseChunkChoiceDeltaImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
