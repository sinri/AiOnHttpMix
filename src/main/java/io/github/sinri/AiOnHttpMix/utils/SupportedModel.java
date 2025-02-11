package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.1.12 changed definitions.
 */
public enum SupportedModel {
    ChatGPT(SupportedProvider.AzureOpenAI),
    QwenPlus(SupportedProvider.DataScope, QwenKit.QwenModel.QWEN_PLUS.getModelCode()),
    QwenMax(SupportedProvider.DataScope, QwenKit.QwenModel.QWEN_MAX.getModelCode()),
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
    public QwenKit.QwenModel asQwenModel() {
        if (provider == SupportedProvider.DataScope) {
            if (mappedModelCode == null) {
                throw new IllegalArgumentException();
            }
            return QwenKit.QwenModel.fromModelCode(mappedModelCode);
        }
        throw new IllegalArgumentException();
    }

    @NotNull
    public DeepseekModel getDeepseekModel() {
        if (provider == SupportedProvider.DeepSeek) {
            if (mappedModelCode == null) {
                throw new IllegalArgumentException();
            }
            return DeepseekModel.fromCode(mappedModelCode);
        }
        throw new IllegalArgumentException();
    }
}
