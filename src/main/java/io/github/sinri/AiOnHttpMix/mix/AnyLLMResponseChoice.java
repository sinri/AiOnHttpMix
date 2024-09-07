package io.github.sinri.AiOnHttpMix.mix;

import java.util.List;

/**
 * @since 1.1.0
 */
public interface AnyLLMResponseChoice {
    static AnyLLMResponseChoice build(String finishReason, String content, List<AnyLLMResponseToolFunctionCall> functionCalls) {
        return new AnyLLMResponseChoiceImpl(finishReason, content, functionCalls);
    }

    String getFinishReason();

    String getContent();

    List<AnyLLMResponseToolFunctionCall> getFunctionCalls();
}
