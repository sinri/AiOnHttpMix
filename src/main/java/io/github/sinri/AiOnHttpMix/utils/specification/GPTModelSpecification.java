package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.ChatGPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

/**
 * 表示ChatGPT模型系列的实现。
 * 该类为单例，名称为"ChatGPT"。
 */
public class GPTModelSpecification implements ModelSpecification {
    /**
     * 模型系列名称常量。
     */
    public static final String SPECIFICATION_NAME = "ChatGPT";

    GPTModelSpecification() {
    }

    /**
     * 获取服务提供方。
     *
     * @return Azure OpenAI服务提供方
     */
    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.azureOpenAI;
    }

    /**
     * 获取模型系列名称。
     *
     * @return 模型系列名称
     */
    @Override
    public String getSpecificationName() {
        return SPECIFICATION_NAME;
    }


}
