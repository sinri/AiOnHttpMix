package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Open AI定义的大语言模型接口规格。
 * 通用于GPT系列模型服务。
 *
 * @since 2.0.0
 */
public abstract class GPTModelSpecification implements ModelSpecification {

    public static final String SPECIFICATION_NAME = "ChatGPT";


    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.azureOpenAI;
    }


    @Override
    public String getSpecificationName() {
        return SPECIFICATION_NAME;
    }

    /**
     * According to the sample `sample.config.properties`, here `config` is extracted from
     * {@code provider.azure.openai}.
     *
     * @param config 针对提供服务的大语言模型在线服务提供商和相关大语言模型服务接口规格进行大语言模型服务适配器构建所需的配置
     */
    @Override
    public ServiceAdapter buildServiceAdapter(KeelConfigElement config) {
        Map<String, OpenAIConfigElement> map = config.getChildren()
                                                     .entrySet()
                                                     .stream()
                                                     .collect(Collectors.toMap(
                                                             Map.Entry::getKey,
                                                             entry -> new OpenAIConfigElement(entry.getValue())
                                                     ));
        return new GPTServiceAdapter(map);
    }
}
