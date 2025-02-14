package io.github.sinri.AiOnHttpMix.mix;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @since 1.1.0
 */
class AnyLLMResponseChoiceImpl implements AnyLLMResponseChoice {
    private final String finishReason;
    private final String content;
    private final List<AnyLLMResponseToolFunctionCall> functionCalls;
    /**
     * @since 1.2.3
     */
    private final String reasoningContent;

    public AnyLLMResponseChoiceImpl(
            String finishReason,
            String content,
            List<AnyLLMResponseToolFunctionCall> functionCalls,
            @Nullable String reasoningContent
    ) {
        this.finishReason = finishReason;
        this.content = content;
        this.functionCalls = functionCalls;
        this.reasoningContent = reasoningContent;
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
    public @NotNull List<AnyLLMResponseToolFunctionCall> getFunctionCalls() {
        return functionCalls;
    }

    /**
     * @since 1.2.3
     */
    @Override
    public @Nullable String getReasoningContent() {
        return reasoningContent;
    }

    /**
     * @since 1.1.1
     */
    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
