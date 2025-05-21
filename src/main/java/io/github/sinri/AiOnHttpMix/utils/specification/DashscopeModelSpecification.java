package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

/**
 * 表示Dashscope模型系列的实现。
 * 该类为单例，名称为"Dashscope"。
 *
 * @since 2.0.0
 */
public abstract class DashscopeModelSpecification implements ModelSpecification {
    /**
     * 模型系列名称常量。
     */
    public static final String SPECIFICATION_NAME = "Dashscope";


    /**
     * 获取服务提供方。
     *
     * @return Dashscope服务提供方
     */
    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.dashscope;
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

    @Override
    public ChatModelServiceAdapter buildServiceAdapter(KeelConfigElement config) {
        String apiKey = config.readString(List.of("apiKey"));
        return new QwenServiceAdapter(apiKey);
    }
}
