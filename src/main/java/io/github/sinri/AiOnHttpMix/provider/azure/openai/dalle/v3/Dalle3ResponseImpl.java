package io.github.sinri.AiOnHttpMix.provider.azure.openai.dalle.v3;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class Dalle3ResponseImpl extends UnmodifiableJsonifiableEntityImpl implements Dalle3Response {
    public Dalle3ResponseImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
