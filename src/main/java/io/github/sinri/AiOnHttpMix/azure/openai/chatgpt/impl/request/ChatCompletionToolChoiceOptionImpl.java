package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.request;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ChatCompletionToolChoiceOptionImpl implements ChatGPTKit.ChatCompletionsParameters.ChatCompletionToolChoiceOption {
    private final Type type;
    private final @Nullable String functionName;

    public ChatCompletionToolChoiceOptionImpl(@NotNull Type type) {
        if (type == Type.function) throw new IllegalArgumentException();
        this.type = type;
        this.functionName = null;
    }

    public ChatCompletionToolChoiceOptionImpl(@NotNull String functionName) {
        this.type = Type.function;
        this.functionName = functionName;
    }

    public Type getType() {
        return type;
    }

    public @Nullable String getFunctionName() {
        return functionName;
    }
}
