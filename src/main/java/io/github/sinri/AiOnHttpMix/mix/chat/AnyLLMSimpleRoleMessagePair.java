package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.vertx.core.json.JsonObject;

/**
 * @param role
 * @param message
 * @since 1.1.5 rename from MessageItem to AnyLLMSimpleRoleMessagePair.
 * @since 1.1.10 add io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMSimpleRoleMessagePair#toJsonObject()
 */
public record AnyLLMSimpleRoleMessagePair(AnyLLMRole role, String message) {
    /**
     * @since 1.1.10
     */
    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("role", role.name())
                .put("message", message);
    }
}
