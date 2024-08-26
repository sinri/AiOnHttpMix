package io.github.sinri.AiOnHttpMix.azure.openai.dalle.impl;

import io.github.sinri.AiOnHttpMix.azure.openai.dalle.Dalle3Kit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class Dalle3ResponseImpl extends UnmodifiableJsonifiableEntityImpl implements Dalle3Kit.Dalle3Response {
    public Dalle3ResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
