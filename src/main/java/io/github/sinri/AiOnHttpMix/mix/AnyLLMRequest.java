package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.vertx.core.Handler;

public interface AnyLLMRequest {
    static AnyLLMRequest create() {
        return new AnyLLMRequestImpl();
    }

    static AnyLLMRequest create(String requestId) {
        return new AnyLLMRequestImpl(requestId);
    }

    String getRequestId();

    default AnyLLMRequest addFunctionToolDefinition(Handler<AnyLLMFunctionToolDefinition.Builder> builderHandler) {
        AnyLLMFunctionToolDefinition.Builder builder = AnyLLMFunctionToolDefinition.builder();
        builderHandler.handle(builder);
        AnyLLMFunctionToolDefinition anyLLMFunctionToolDefinition = builder.build();
        return addFunctionToolDefinition(anyLLMFunctionToolDefinition);
    }

    AnyLLMRequest addFunctionToolDefinition(AnyLLMFunctionToolDefinition functionToolDefinition);

    AnyLLMRequest addSystemMessage(String systemMessage);

    AnyLLMRequest addUserMessage(String userMessage);

    OpenAIChatGptRequest toChatGptRequest();

    QwenRequest toQwenRequest();

    VolcesChatRequest toVolcesChatRequest();
}
