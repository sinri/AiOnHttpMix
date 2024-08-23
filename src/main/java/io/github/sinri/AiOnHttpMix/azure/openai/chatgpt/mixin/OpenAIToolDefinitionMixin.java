package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface OpenAIToolDefinitionMixin<E> extends JsonifiableEntity<E> {
    enum Type {
        function
    }

    interface Builder {
        Builder functionName(String functionName);

        Builder functionDescription(String functionDescription);

        Builder propertyAsString(String name, String desc);

        Builder propertyAsInt(String name, String desc);

        Builder propertyAsNumber(String name, String desc);

        Builder propertyAsBoolean(String name, String desc);

        ChatGPTKit.ToolDefinition build();
    }
}
