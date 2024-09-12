package io.github.sinri.AiOnHttpMix.mix;

/**
 * @since 1.1.0
 */
class AnyLLMResponseToolFunctionCallImpl implements AnyLLMResponseToolFunctionCall {
    String functionName;
    String functionArguments;

    public AnyLLMResponseToolFunctionCallImpl(String functionName, String functionArguments) {
        this.functionArguments = functionArguments;
        this.functionName = functionName;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getFunctionArguments() {
        return functionArguments;
    }

    /**
     * @since 1.1.1
     */
    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
