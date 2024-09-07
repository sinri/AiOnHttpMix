package io.github.sinri.AiOnHttpMix.mix;

public interface AnyLLMResponseToolFunctionCall {
    static AnyLLMResponseToolFunctionCall build(String functionName, String functionArguments) {
        return new AnyLLMResponseToolFunctionCallImpl(functionName, functionArguments);
    }

    String getFunctionName();

    String getFunctionArguments();
}
