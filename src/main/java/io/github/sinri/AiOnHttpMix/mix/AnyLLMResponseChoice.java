package io.github.sinri.AiOnHttpMix.mix;

import java.util.List;

public interface AnyLLMResponseChoice {
    static AnyLLMResponseChoice build(String finishReason, String content, List<AnyLLMResponseToolFunctionCall> functionCalls) {
        return new AnyLLMResponseChoiceImpl(finishReason, content, functionCalls);
    }

    String getFinishReason();

    String getContent();

    List<AnyLLMResponseToolFunctionCall> getFunctionCalls();
}
