package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenModel;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 大模型服务平台提供的同一系列的大模型。
 * <p>
 * 同一系列的大模型是指调用参数一致，仅通过动态参数差异来实现目标模型实例的选取。
 * <p>
 * 如GPT、豆包系列模型下，有不同版本不同规模参数量的部署，通过使用不同参数决定最终使用的模型。
 *
 * @since 1.1.12 changed definitions.
 */
public enum SupportedModel {
    /**
     * Azure OpenAI ChatGPT系列，通过部署名称来指定运行的模型分支。
     */
    ChatGPT(SupportedProvider.AzureOpenAI),
    QwenPlus(SupportedProvider.DashScope, QwenModel.QWEN_PLUS.getModelCode()),
    QwenPlusLatest(SupportedProvider.DashScope, QwenModel.QWEN_PLUS_LATEST.getModelCode()),
    QwenMax(SupportedProvider.DashScope, QwenModel.QWEN_MAX.getModelCode()),
    QwenMaxLatest(SupportedProvider.DashScope, QwenModel.QWEN_MAX_LATEST.getModelCode()),
    QwenTurbo(SupportedProvider.DashScope, QwenModel.QWEN_TURBO.getModelCode()),
    QwenTurboLatest(SupportedProvider.DashScope, QwenModel.QWEN_TURBO_LATEST.getModelCode()),
    /**
     * @since 1.2.2
     */
    QwenLong(SupportedProvider.DashScope, QwenModel.QWEN_LONG.getModelCode()),
    DeepSeekReasonerOnDashScope(SupportedProvider.DashScope, "deepseek-r1"),
    DeepSeekChatOnDashScope(SupportedProvider.DashScope, "deepseek-v3"),
    /**
     * Once named as `Volces` 字节的豆包系列，通过部署名称来指定运行的模型分支。
     */
    Doubao(SupportedProvider.Volces),
    /**
     * 字节的月之暗面系列，通过部署名称来指定运行的模型分支。
     *
     * @since 1.2.2
     */
    KimiOnVolces(SupportedProvider.Volces),
    DeepSeekReasonerOnVolces(SupportedProvider.Volces),
    DeepSeekChatOnVolces(SupportedProvider.Volces),

    DeepSeekChat(SupportedProvider.DeepSeek, DeepseekModel.ChatModel.getCode()),
    DeepSeekReasoner(SupportedProvider.DeepSeek, DeepseekModel.ReasonerModel.getCode()),
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
