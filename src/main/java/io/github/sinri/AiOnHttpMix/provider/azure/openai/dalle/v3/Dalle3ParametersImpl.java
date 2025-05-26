package io.github.sinri.AiOnHttpMix.provider.azure.openai.dalle.v3;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.dalle.Dalle3Kit;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class Dalle3ParametersImpl implements Dalle3Parameters {
    private JsonObject jsonObject;

    public Dalle3ParametersImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Dalle3ParametersImpl() {
        this.jsonObject = new JsonObject();
        setN(1);
        setQuality(Dalle3Kit.Dalle3Quality.standard);
        setStyle(Dalle3Kit.Dalle3Style.natural);
        setSize(Dalle3Kit.Dalle3Size.SQUARE);
    }

    @Override
    public @Nonnull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public @Nonnull Dalle3Parameters reloadDataFromJsonObject(@Nonnull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    @Nonnull
    @Override
    public Dalle3Parameters getImplementation() {
        return this;
    }
}
