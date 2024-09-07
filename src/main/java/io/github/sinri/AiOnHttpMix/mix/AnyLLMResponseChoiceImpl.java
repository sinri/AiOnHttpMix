package io.github.sinri.AiOnHttpMix.mix;

import java.util.List;

class AnyLLMResponseChoiceImpl implements AnyLLMResponseChoice {
    private final String finishReason;
    private final String content;
    private final List<AnyLLMResponseToolFunctionCall> functionCalls;

    public AnyLLMResponseChoiceImpl(String finishReason, String content, List<AnyLLMResponseToolFunctionCall> functionCalls) {
        this.finishReason = finishReason;
        this.content = content;
        this.functionCalls = functionCalls;
    }

    @Override
    public String getFinishReason() {
        return finishReason;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public List<AnyLLMResponseToolFunctionCall> getFunctionCalls() {
        return functionCalls;
    }
}
