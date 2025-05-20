package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;

/**
 * For Azure OpenAI o-series models.
 */
public class OModelSpecification implements ModelSpecification {
    /**
     * 模型系列名称常量。
     */
    public static final String SPECIFICATION_NAME = "O";

    OModelSpecification() {
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
