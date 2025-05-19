package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.tool;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolDefinition;
import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class ChatGPTFunctionToolDefinition extends JsonifiableEntityImpl<FunctionToolDefinition> implements FunctionToolDefinition {
    public ChatGPTFunctionToolDefinition(){
        super();
    }
    public ChatGPTFunctionToolDefinition(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public FunctionToolDefinition getImplementation() {
        return this;
    }
}
