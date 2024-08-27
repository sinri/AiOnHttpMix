package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request;

import org.jetbrains.annotations.Nullable;

public interface OpenAIChatGptRequestToolChoiceOption {
    static OpenAIChatGptRequestToolChoiceOption asNone() {
        return new OpenAIChatGptRequestToolChoiceOptionImpl(Type.none);
    }

    static OpenAIChatGptRequestToolChoiceOption asAuto() {
        return new OpenAIChatGptRequestToolChoiceOptionImpl(Type.auto);
    }

    static OpenAIChatGptRequestToolChoiceOption asFunction(String functionName) {
        return new OpenAIChatGptRequestToolChoiceOptionImpl(functionName);
    }

    Type getType();

    @Nullable String getFunctionName();

    /**
     * This enum is not defined by OpenAI but Sinri.
     */
    enum Type {
        none, auto, function
    }
}
