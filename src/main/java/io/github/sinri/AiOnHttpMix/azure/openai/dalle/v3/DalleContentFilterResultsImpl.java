package io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class DalleContentFilterResultsImpl extends UnmodifiableJsonifiableEntityImpl implements DalleContentFilterResults {
    public DalleContentFilterResultsImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
