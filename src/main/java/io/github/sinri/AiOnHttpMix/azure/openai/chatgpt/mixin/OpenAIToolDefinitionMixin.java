package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface OpenAIToolDefinitionMixin<E> extends JsonifiableEntity<E> {
    enum Type {
        function
    }

    @Deprecated
    interface FunctionToolDefinitionBuilder {
        FunctionToolDefinitionBuilder functionName(String functionName);

        FunctionToolDefinitionBuilder functionDescription(String functionDescription);

        FunctionToolDefinitionBuilder propertyAsString(String name, String desc);

        FunctionToolDefinitionBuilder propertyAsInt(String name, String desc);

        FunctionToolDefinitionBuilder propertyAsNumber(String name, String desc);

        FunctionToolDefinitionBuilder propertyAsBoolean(String name, String desc);

        ChatGPTKit.ToolDefinition build();
    }
}
