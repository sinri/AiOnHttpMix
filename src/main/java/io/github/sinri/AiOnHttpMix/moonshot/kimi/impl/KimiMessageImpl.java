package io.github.sinri.AiOnHttpMix.moonshot.kimi.impl;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.KimiKit;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class KimiMessageImpl implements KimiKit.Message {
    private JsonObject jsonObject;

    public KimiMessageImpl(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public KimiMessageImpl() {
        this.jsonObject = new JsonObject();
    }

    @Override
    public @NotNull KimiKit.Message getImplementation() {
        return this;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return jsonObject;
    }

    @Override
    public @NotNull KimiKit.Message reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
}
