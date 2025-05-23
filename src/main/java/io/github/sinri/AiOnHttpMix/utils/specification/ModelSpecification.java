package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

/**
 * 本接口定义了一种大语言模型接口规格。
 * 其由某一大语言模型在线服务提供商支持，针对若干模型实例采用统一的接口输入输出格式定义，以利不同规格或风格的大语言模型在同一体系下进行推理。
 *
 * @since 2.0.0
 */
public interface ModelSpecification {
    /**
     * @return 基于此模型规格提供服务的大语言模型在线服务提供商
     */
    ServiceProvider getServiceProvider();

    /**
     * @return 本大语言模型接口规格的名称
     */
    String getSpecificationName();

    /**
     * 针对提供服务的大语言模型在线服务提供商和相关大语言模型服务接口规格，根据配置进行大语言模型服务适配器构建。
     *
     * @param config 针对提供服务的大语言模型在线服务提供商和相关大语言模型服务接口规格进行大语言模型服务适配器构建所需的配置
     * @return 构建完成的大语言模型服务适配器
     */
    ServiceAdapter buildServiceAdapter(KeelConfigElement config);
}
