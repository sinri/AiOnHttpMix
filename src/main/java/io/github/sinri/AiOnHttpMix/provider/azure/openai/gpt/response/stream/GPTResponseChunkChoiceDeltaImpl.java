package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.stream;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class GPTResponseChunkChoiceDeltaImpl extends UnmodifiableJsonifiableEntityImpl implements GPTResponseChunkChoiceDelta {
    public GPTResponseChunkChoiceDeltaImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
