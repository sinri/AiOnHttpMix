package io.github.sinri.AiOnHttpMix.mix.vl;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @since 1.3.0
 */
public record AnyVLLMRoleMessagePair(AnyLLMRole role, List<AnyVLLMMessageComponent> content) {

    public JsonObject toJsonObject() {
        JsonArray array = new JsonArray();
        content.forEach(item -> {
            array.add(item.toJsonObject());
        });
        return new JsonObject()
                .put("role", role.name())
                .put("content", array);
    }
}
