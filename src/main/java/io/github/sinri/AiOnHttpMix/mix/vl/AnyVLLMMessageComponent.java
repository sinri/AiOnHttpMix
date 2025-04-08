package io.github.sinri.AiOnHttpMix.mix.vl;

import io.vertx.core.json.JsonObject;

/**
 * @since 1.3.0
 */
public record AnyVLLMMessageComponent(AnyVLLMMessageComponentType type, String value) {
    public JsonObject toJsonObject() {
        return new JsonObject()
                .put(type.name(), value);
    }
}
