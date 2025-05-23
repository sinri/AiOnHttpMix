package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

/**
 * Dashscope定义的大语言模型接口规格。
 * 通用于通义千问（Qwen）系列模型服务。
 *
 * @since 2.0.0
 */
public abstract class DashscopeModelSpecification implements ModelSpecification {

    public static final String SPECIFICATION_NAME = "Dashscope";

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.dashscope;
    }

    @Override
    public String getSpecificationName() {
        return SPECIFICATION_NAME;
    }

    /**
     * According to the sample `sample.config.properties`, here `config` is extracted from
     * {@code provider.dashscope.qwen}.
     *
     * @param config 针对提供服务的大语言模型在线服务提供商和相关大语言模型服务接口规格进行大语言模型服务适配器构建所需的配置
     */
    @Override
    public ServiceAdapter buildServiceAdapter(KeelConfigElement config) {
        String apiKey = config.readString(List.of("apiKey"));
        return new QwenServiceAdapter(apiKey);
    }
}
