package io.github.sinri.AiOnHttpMix.azure.openai.dalle.v3;

import io.github.sinri.AiOnHttpMix.azure.openai.dalle.Dalle3Kit;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface Dalle3Parameters extends JsonifiableEntity<Dalle3Parameters> {
    static Dalle3Parameters create() {
        return new Dalle3ParametersImpl();
    }

    static Dalle3Parameters wrap(JsonObject jsonObject) {
        return new Dalle3ParametersImpl(jsonObject);
    }

    default Dalle3Parameters setPrompt(String prompt) {
        this.toJsonObject().put("prompt", prompt);
        return this;
    }

    default Dalle3Parameters setSize(Dalle3Kit.Dalle3Size size) {
        this.toJsonObject().put("size", size.size());
        return this;
    }

    default Dalle3Parameters setQuality(Dalle3Kit.Dalle3Quality quality) {
        this.toJsonObject().put("quality", quality.name());
        return this;
    }

    default Dalle3Parameters setStyle(Dalle3Kit.Dalle3Style style) {
        this.toJsonObject().put("style", style.name());
        return this;
    }

    default Dalle3Parameters setN(int n) {
        this.toJsonObject().put("n", n);
        return this;
    }
}
