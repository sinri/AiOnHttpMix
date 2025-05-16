package io.github.sinri.AiOnHttpMix.utils.series;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.ChatGPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

/**
 * 表示ChatGPT模型系列的实现。
 * 该类为单例，名称为"ChatGPT"。
 */
public final class ChatGPTModelSeries implements ChatModelSeries {
    /**
     * 模型系列名称常量。
     */
    public static final String NAME = "ChatGPT";

    ChatGPTModelSeries() {
    }

    /**
     * 获取服务提供方。
     * @return Azure OpenAI服务提供方
     */
    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.azureOpenAI;
    }

    /**
     * 获取模型系列名称。
     * @return 模型系列名称
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * 构建服务元信息。
     * @param config 配置项
     * @return ChatGPT服务适配器
     */
    @Override
    public ChatModelServiceAdapter buildServiceAdapter(KeelConfigElement config) {
        String apiKey = config.readString(List.of("apiKey"));
        String resourceName = config.readString(List.of("resourceName"));
        String deployment = config.readString(List.of("deployment"));
        String apiVersion = config.readString(List.of("apiVersion"));
        return new ChatGPTServiceAdapter(apiKey, resourceName, deployment, apiVersion);
    }
}
