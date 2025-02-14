package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.vertx.core.Handler;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.1.0
 */
public interface AnyLLMRequest {
    static AnyLLMRequest create() {
        return new AnyLLMRequestImpl();
    }

    static AnyLLMRequest create(String requestId) {
        return new AnyLLMRequestImpl(requestId);
    }

    String getRequestId();

    /**
     * @since 1.1.2
     */
    default AnyLLMRequest addFunctionToolDefinition(FunctionCallAdapter functionCallAdapter) {
        return addFunctionToolDefinition(functionCallAdapter.toFunctionToolDefinition());
    }

    /**
     * @since 1.1.2
     */
    default AnyLLMRequest addFunctionToolDefinition(AnyLLMKit anyLLMKit, String registeredFunctionName) {
        FunctionCallAdapter registeredFunction = anyLLMKit.getRegisteredFunction(registeredFunctionName);
        if (registeredFunction == null) {
            throw new IllegalArgumentException("No such function: " + registeredFunctionName);
        }
        return addFunctionToolDefinition(registeredFunction.toFunctionToolDefinition());
    }

    default AnyLLMRequest addFunctionToolDefinition(Handler<AnyLLMFunctionToolDefinition.Builder> builderHandler) {
        AnyLLMFunctionToolDefinition.Builder builder = AnyLLMFunctionToolDefinition.builder();
        builderHandler.handle(builder);
        AnyLLMFunctionToolDefinition anyLLMFunctionToolDefinition = builder.build();
        return addFunctionToolDefinition(anyLLMFunctionToolDefinition);
    }

    AnyLLMRequest addFunctionToolDefinition(AnyLLMFunctionToolDefinition functionToolDefinition);

    default AnyLLMRequest addSystemMessage(String systemMessage) {
        return this.addRoleMessage(AnyLLMRole.system, systemMessage);
    }

    default AnyLLMRequest addUserMessage(String userMessage) {
        return this.addRoleMessage(AnyLLMRole.user, userMessage);
    }

    /**
     * @since 1.1.5
     */
    AnyLLMRequest addRoleMessage(AnyLLMRole role, String roleMessage);

    /**
     * @since 1.2.2
     */
    @Nullable
    AnyLLMExtraOptions getExtraOptions();

    /**
     * @since 1.2.2
     */
    AnyLLMRequest setExtraOptions(@Nullable AnyLLMExtraOptions extraOptions);

    OpenAIChatGptRequest toChatGptRequest();

    QwenRequest toQwenRequest();

    VolcesChatRequest toVolcesChatRequest();

    /**
     * @since 1.1.12
     */
    DeepseekChatRequest toDeepseekChatRequest();

    /**
     * @since 1.1.5
     */
    MirageRequestEntity toMirageRequestEntity();

    /**
     * @since 1.2.2
     */
    int getMaxExecutionSeconds();

    /**
     * @since 1.2.2
     */
    AnyLLMRequest setMaxExecutionSeconds(int maxExecutionSeconds);
}
