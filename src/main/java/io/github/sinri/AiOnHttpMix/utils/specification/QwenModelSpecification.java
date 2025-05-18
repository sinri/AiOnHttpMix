package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

/**
 * 表示Qwen模型系列的实现。
 * 该类为单例，名称为"Qwen"。
 *
 * @since 2.0.0
 */
public class QwenModelSpecification implements ModelSpecification {
    /**
     * 模型系列名称常量。
     */
    public static final String SPECIFICATION_NAME = "Qwen";

    QwenModelSpecification() {

    }

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


}
