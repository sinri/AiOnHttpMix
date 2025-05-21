package io.github.sinri.AiOnHttpMix.utils.providers;

/**
 * 大语言模型在线服务提供商：微软旗下的OpenAI服务（Azure OpenAI）
 *
 * @since 2.0.0
 */
public final class AzureOpenAIServiceProvider implements ServiceProvider {
    public static final String PROVIDER_NAME = "AzureOpenAI";

    AzureOpenAIServiceProvider() {
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
}
