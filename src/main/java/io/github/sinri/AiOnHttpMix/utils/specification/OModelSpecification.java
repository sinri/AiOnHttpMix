package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.o.OServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * For Azure OpenAI o-series models.
 */
public abstract class OModelSpecification implements ModelSpecification {
    /**
     * 模型系列名称常量。
     */
    public static final String SPECIFICATION_NAME = "O";


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

    /**
     * This method is not tested yet.
     */
    @Override
    public ChatModelServiceAdapter buildServiceAdapter(KeelConfigElement config) {
        Map<String, OpenAIConfigElement> map = config.getChildren()
                                                     .entrySet()
                                                     .stream()
                                                     .collect(Collectors.toMap(
                                                             Map.Entry::getKey,
                                                             entry -> new OpenAIConfigElement(entry.getValue())
                                                     ));
        return new OServiceAdapter(map);
    }
}
