package io.github.sinri.AiOnHttpMix.mix.chat;

import io.vertx.core.json.JsonObject;

/**
 * @since 1.1.0
 */
public interface AnyLLMResponseToolFunctionCall {
    static AnyLLMResponseToolFunctionCall build(String functionName, String functionArguments) {
        return new AnyLLMResponseToolFunctionCallImpl(functionName, functionArguments);
    }

    /**
     * @param jsonObject
     * @return
     * @since 1.1.4
     */
    static AnyLLMResponseToolFunctionCall wrap(JsonObject jsonObject) {
        return build(
                jsonObject.getString("function_name"),
                jsonObject.getString("function_arguments")
        );
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
