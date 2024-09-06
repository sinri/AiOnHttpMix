package io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class Dalle3ResponseImpl extends UnmodifiableJsonifiableEntityImpl implements Dalle3Response {
    public Dalle3ResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
