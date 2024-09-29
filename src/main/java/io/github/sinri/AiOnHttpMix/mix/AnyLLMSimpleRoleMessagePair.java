package io.github.sinri.AiOnHttpMix.mix;

/**
 * @param role
 * @param message
 * @since 1.1.5 rename from MessageItem to AnyLLMSimpleRoleMessagePair.
 */
public record AnyLLMSimpleRoleMessagePair(AnyLLMRole role, String message) {
}
