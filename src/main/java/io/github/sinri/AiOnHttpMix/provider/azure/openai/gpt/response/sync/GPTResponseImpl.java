package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class GPTResponseImpl extends UnmodifiableJsonifiableEntityImpl implements GPTResponse {
    public GPTResponseImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
