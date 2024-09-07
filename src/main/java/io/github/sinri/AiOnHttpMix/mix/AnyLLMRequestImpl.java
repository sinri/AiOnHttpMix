package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptToolDefinition;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class AnyLLMRequestImpl implements AnyLLMRequest {
    private final String requestId;
    private final List<MessageItem> messageItems = new ArrayList<>();
    private final List<AnyLLMFunctionToolDefinition> functionToolDefinitions = new ArrayList<>();

    public AnyLLMRequestImpl() {
        this.requestId = UUID.randomUUID().toString();
    }

    public AnyLLMRequestImpl(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    public AnyLLMRequest addFunctionToolDefinition(AnyLLMFunctionToolDefinition functionToolDefinition) {
        functionToolDefinitions.add(functionToolDefinition);
        return this;
    }

    @Override
    public AnyLLMRequest addSystemMessage(String systemMessage) {
        messageItems.add(new MessageItem(AnyLLMRole.system, systemMessage));
        return this;
    }

    @Override
    public AnyLLMRequest addUserMessage(String userMessage) {
        messageItems.add(new MessageItem(AnyLLMRole.user, userMessage));
        return this;
    }

    @Override
    public OpenAIChatGptRequest toChatGptRequest() {
        OpenAIChatGptRequest req = OpenAIChatGptRequest.create();
        for (MessageItem messageItem : messageItems) {
            switch (messageItem.role) {
                case system:
                    req.addMessage(b -> b.system(messageItem.message));
                    break;
                case user:
                    req.addMessage(b -> b.user(messageItem.message));
            }
        }
        for (var f : functionToolDefinitions) {
            req.addTool(OpenAIChatGptToolDefinition.wrap(f.toJsonObject()));
        }
        return req;
    }

    @Override
    public QwenRequest toQwenRequest() {
        return null;//todo
    }

    @Override
    public VolcesChatRequest toVolcesChatRequest() {
        return null;//todo
    }

    public record MessageItem(AnyLLMRole role, String message) {
    }
}
