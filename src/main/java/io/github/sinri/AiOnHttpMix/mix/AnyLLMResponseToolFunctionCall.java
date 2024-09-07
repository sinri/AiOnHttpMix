package io.github.sinri.AiOnHttpMix.mix;

/**
 * @since 1.1.0
 */
public interface AnyLLMResponseToolFunctionCall {
    static AnyLLMResponseToolFunctionCall build(String functionName, String functionArguments) {
        return new AnyLLMResponseToolFunctionCallImpl(functionName, functionArguments);
    }

    String getFunctionName();

    String getFunctionArguments();
}
