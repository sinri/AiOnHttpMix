package io.github.sinri.AiOnHttpMix.mix;

import io.vertx.core.json.JsonObject;

/**
 * @since 1.1.0
 */
public interface AnyLLMResponseToolFunctionCall {
    static AnyLLMResponseToolFunctionCall build(String functionName, String functionArguments) {
        return new AnyLLMResponseToolFunctionCallImpl(functionName, functionArguments);
    }

    String getFunctionName();

    String getFunctionArguments();

    /**
     * @since 1.1.1
     */
    default JsonObject toJsonObject() {
        return new JsonObject()
                .put("function_name", getFunctionName())
                .put("function_arguments", getFunctionArguments());
    }
}
