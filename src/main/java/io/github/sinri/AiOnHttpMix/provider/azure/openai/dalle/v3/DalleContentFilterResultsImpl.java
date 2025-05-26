package io.github.sinri.AiOnHttpMix.provider.azure.openai.dalle.v3;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class DalleContentFilterResultsImpl extends UnmodifiableJsonifiableEntityImpl implements DalleContentFilterResults {
    public DalleContentFilterResultsImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
