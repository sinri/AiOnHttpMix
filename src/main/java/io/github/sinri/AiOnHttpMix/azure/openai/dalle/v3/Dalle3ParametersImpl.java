package io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3;

import io.github.sinri.AiOnHttpMix.azure.openai.dalle.Dalle3Kit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public @NotNull Dalle3Parameters reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
