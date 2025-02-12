package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenModel;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.1.12 changed definitions.
 */
public enum SupportedModel {
    ChatGPT(SupportedProvider.AzureOpenAI),
    QwenPlus(SupportedProvider.DashScope, QwenModel.QWEN_PLUS.getModelCode()),
    QwenMax(SupportedProvider.DashScope, QwenModel.QWEN_MAX.getModelCode()),
    DeepSeekReasonerOnDashScope(SupportedProvider.DashScope, "deepseek-r1"),
    DeepSeekChatOnDashScope(SupportedProvider.DashScope, "deepseek-v3"),
    /**
     * Once named as `Volces`
     */
    Doubao(SupportedProvider.Volces),
    DeepSeekReasonerOnVolces(SupportedProvider.Volces),
    DeepSeekChatOnVolces(SupportedProvider.Volces),

    // DeepSeekChat(SupportedProvider.DeepSeek, DeepseekModel.ChatModel.getCode()),
    // DeepSeekReasoner(SupportedProvider.DeepSeek, DeepseekModel.ReasonerModel.getCode()),
    ;
    private final @NotNull SupportedProvider provider;
    private final @Nullable String mappedModelCode;

    SupportedModel(@NotNull SupportedProvider provider) {
        this.provider = provider;
        this.mappedModelCode = null;
    }

    SupportedModel(@NotNull SupportedProvider provider, @NotNull String mappedModelCode) {
        this.provider = provider;
        this.mappedModelCode = mappedModelCode;
    }

    public @NotNull SupportedProvider getProvider() {
        return provider;
    }

    public @Nullable String getMappedModelCode() {
        return mappedModelCode;
    }

    @NotNull
    @Deprecated(since = "1.2.2", forRemoval = true)
    public QwenModel asQwenModel() {
        // todo remove this logic
        if (provider == SupportedProvider.DashScope) {
            if (mappedModelCode == null) {
                throw new IllegalArgumentException();
            }
            return QwenModel.fromModelCode(mappedModelCode);
        }
        throw new IllegalArgumentException();
    }

    @NotNull
    @Deprecated(since = "1.2.2", forRemoval = true)
    public DeepseekModel asDeepseekModel() {
        if (provider == SupportedProvider.DeepSeek) {
            if (mappedModelCode == null) {
                throw new IllegalArgumentException();
            }
            return DeepseekModel.fromCode(mappedModelCode);
        }
        throw new IllegalArgumentException();
    }
}
