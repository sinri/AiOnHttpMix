package io.github.sinri.AiOnHttpMix.utils;

/**
 * @param argumentType
 * @param name
 * @param desc
 * @since 1.1.2
 */
public record FunctionToolArgumentDefinition(
        FunctionToolArgumentType argumentType,
        String name,
        String desc
) {

}
