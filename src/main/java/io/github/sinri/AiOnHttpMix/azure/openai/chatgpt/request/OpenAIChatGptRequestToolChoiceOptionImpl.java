package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class OpenAIChatGptRequestToolChoiceOptionImpl implements OpenAIChatGptRequestToolChoiceOption {
    private final Type type;
    private final @Nullable String functionName;

    public OpenAIChatGptRequestToolChoiceOptionImpl(@NotNull Type type) {
        if (type == Type.function) throw new IllegalArgumentException();
        this.type = type;
        this.functionName = null;
    }

    public OpenAIChatGptRequestToolChoiceOptionImpl(@NotNull String functionName) {
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
