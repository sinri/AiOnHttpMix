package io.github.sinri.AiOnHttpMix.utils;

/**
 * 提供大模型服务的平台。
 * <p>
 * Once named as `SupportedModelSeries`.
 * </p>
 *
 * @since 1.1.12 changed definitions.
 */
public enum SupportedProvider {
    /**
     * Once named as `ChatGPT`
     */
    AzureOpenAI,
    /**
     * Once named as `Qwen`
     */
    DashScope,
    Volces,
    DeepSeek,
    /**
     * Once named `Kimi`.
     */
    Moonshot,
}
