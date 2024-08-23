package io.github.sinri.AiOnHttpMix.azure.openai.dalle.impl;

import io.github.sinri.AiOnHttpMix.azure.openai.dalle.Dalle3Kit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class DalleContentFilterResultsImpl extends UnmodifiableJsonifiableEntityImpl implements Dalle3Kit.DalleContentFilterResults {
    public DalleContentFilterResultsImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
