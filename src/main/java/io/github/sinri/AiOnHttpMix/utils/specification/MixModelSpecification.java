package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

public class MixModelSpecification implements ModelSpecification {
    public static final String SPECIFICATION_NAME = "Mix";

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }

    @Override
    public String getSpecificationName() {
        return SPECIFICATION_NAME;
    }

    /**
     * According to the sample `sample.config.properties`, here `config` is extracted from
     * {@code provider}.
     *
     * @param config 针对提供服务的大语言模型在线服务提供商和相关大语言模型服务接口规格进行大语言模型服务适配器构建所需的配置
     */
    @Override
    public ServiceAdapter buildServiceAdapter(KeelConfigElement config) {
        // return new NativeMixServiceAdapter(config);
        // todo through Mirage Provider
        throw new RuntimeException("todo");
    }
}
