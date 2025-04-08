package io.github.sinri.AiOnHttpMix.mix.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptToolDefinition;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolDefinition;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInRequest;
import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.tool.VolcesChatFunctionDefinition;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @since 1.1.0
 */
class AnyLLMRequestImpl implements AnyLLMRequest {
    private final String requestId;
    private final List<AnyLLMSimpleRoleMessagePair> messageItems = new ArrayList<>();
    private final List<AnyLLMFunctionToolDefinition> functionToolDefinitions = new ArrayList<>();
    /**
     * @since 1.2.2
     */
    private int maxExecutionSeconds = 180;
    private @Nullable AnyLLMExtraOptions extraOptions = null;

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
    public AnyLLMRequest addRoleMessage(AnyLLMRole role, String roleMessage) {
        messageItems.add(new AnyLLMSimpleRoleMessagePair(role, roleMessage));
        return this;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public @Nullable AnyLLMExtraOptions getExtraOptions() {
        return extraOptions;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public AnyLLMRequest setExtraOptions(@Nullable AnyLLMExtraOptions extraOptions) {
        this.extraOptions = extraOptions;
        return this;
    }

    @Override
    public OpenAIChatGptRequest toChatGptRequest() {
        OpenAIChatGptRequest req = OpenAIChatGptRequest.create();
        for (AnyLLMSimpleRoleMessagePair messageItem : messageItems) {
            switch (messageItem.role()) {
                case system:
                    req.addMessage(b -> b.system(messageItem.message()));
                    break;
                case user:
                    req.addMessage(b -> b.user(messageItem.message()));
            }
        }
        for (var f : functionToolDefinitions) {
            req.addTool(OpenAIChatGptToolDefinition.wrap(f.toJsonObject()));
        }

        if (extraOptions != null) {
            Double temperature = extraOptions.getTemperature();
            if (temperature != null) {
                req.setTemperature(temperature);
            }
            String responseFormat = extraOptions.getResponseFormat();
            if (responseFormat != null) {
                JsonObject x = new JsonObject(responseFormat);
                String type = x.getString("type");
                JsonObject jsonSchema = x.getJsonObject("json_schema");
                req.setResponseFormat(OpenAIChatGptRequest.ChatCompletionResponseFormat.valueOf(type), jsonSchema);
            }
            //  IncrementalOutput is default for Azure OpenAI ChatGPT
        }

        return req;
    }

    @Override
    public QwenRequest toQwenRequest() {
        QwenRequest qwenRequest = QwenRequest.create();
        qwenRequest.handleInput(input -> {
            this.messageItems.forEach(messageItem -> {
                switch (messageItem.role()) {
                    case system:
                        input.addSystemMessage(messageItem.message());
                        break;
                    case user:
                        input.addUserMessage(messageItem.message());
                        break;
                }
            });
        });
        this.functionToolDefinitions.forEach(functionToolDefinition -> {
            qwenRequest.handleParameters(parameters -> {
                parameters.addTool(QwenToolDefinition.wrap(functionToolDefinition.toJsonObject()));
            });
        });
        if (extraOptions != null) {
            Double temperature = extraOptions.getTemperature();
            if (temperature != null) {
                qwenRequest.handleParameters(p -> p.setTemperature(temperature.floatValue()));
            }
            String responseFormat = extraOptions.getResponseFormat();
            if (responseFormat != null) {
                qwenRequest.handleParameters(p -> p.setResultFormat(QwenRequest.Parameters.ResultFormat.valueOf(responseFormat)));
            }
            Boolean incrementalOutput = extraOptions.getIncrementalOutput();
            if (incrementalOutput != null) {
                qwenRequest.handleParameters(p -> p.setIncrementalOutput(incrementalOutput));
            }
        }
        return qwenRequest;
    }

    @Override
    public VolcesChatRequest toVolcesChatRequest() {
        VolcesChatRequest request = VolcesChatRequest.create();
        this.messageItems.forEach(messageItem -> {
            switch (messageItem.role()) {
                case system:
                    request.addMessage(m -> m.setRole(VolcesChatRole.system).setContent(messageItem.message()));
                    break;
                case user:
                    request.addMessage(m -> m.setRole(VolcesChatRole.user).setContent(messageItem.message()));
                    break;
            }
        });
        this.functionToolDefinitions.forEach(functionToolDefinition -> {
            request.addToolAsFunction(VolcesChatFunctionDefinition.wrap(functionToolDefinition.toJsonObject()));
        });
        if (extraOptions != null) {
            Double temperature = extraOptions.getTemperature();
            if (temperature != null) {
                request.setTemperature(temperature);
            }
            // Response Format is not supported
            // Incremental Output is default for Volces
        }
        return request;
    }

    /**
     * @since 1.1.12
     */
    @Override
    public DeepseekChatRequest toDeepseekChatRequest() {
        DeepseekChatRequest chatRequest = DeepseekChatRequest.create();
        this.messageItems.forEach(messageItem -> {
            chatRequest.addMessage(DeepseekMessageInRequest.create()
                                                           .setRole(messageItem.role().toDeepseekRole())
                                                           .setContent(messageItem.message())
            );
        });
        this.functionToolDefinitions.forEach(functionToolDefinition -> {
            chatRequest.addTool(DeepseekChatRequest.ToolDefinition.wrap(functionToolDefinition.toJsonObject()));
        });
        if (extraOptions != null) {
            Double temperature = extraOptions.getTemperature();
            if (temperature != null) {
                chatRequest.setTemperature(temperature);
            }
            String responseFormat = extraOptions.getResponseFormat();
            if (responseFormat != null) {
                chatRequest.setResponseFormatType(DeepseekChatRequest.ResponseFormatType.valueOf(responseFormat));
            }
            // Incremental Output is default for Volces
        }
        return chatRequest;
    }

    /**
     * @since 1.1.5
     */
    @Override
    public MirageRequestEntity toMirageRequestEntity() {
        MirageRequestEntity mirageRequestEntity = new MirageRequestEntity();
        this.messageItems.forEach(mirageRequestEntity::addToPrompt);
        this.functionToolDefinitions.forEach(x -> {
            //            Keel.getLogger().fatal("x", x.toJsonObject());
            //            Keel.getLogger().fatal("x.fn: " + x.getFunctionName());
            //            Keel.getLogger().fatal("x.fd: " + x.getFunctionDescription());
            //            x.getFunctionArgumentDefinitions().forEach(item -> {
            //                Keel.getLogger().fatal("x.a[]: " + item.name() + " as " + item.argumentType() + " // " + item.desc());
            //            });

            mirageRequestEntity.addFunctionDefinition(
                    x.getFunctionName(),
                    x.getFunctionDescription(),
                    x.getFunctionArgumentDefinitions()
            );
        });
        return mirageRequestEntity;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public AnyLLMRequest setMaxExecutionSeconds(int maxExecutionSeconds) {
        this.maxExecutionSeconds = maxExecutionSeconds;
        return this;
    }

    /**
     * @since 1.2.2
     */
    @Override
    public int getMaxExecutionSeconds() {
        return maxExecutionSeconds;
    }
}
