package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 火山引擎定义的大语言模型接口规格。
 * 通用于火山引擎上提供的豆包、DeepSeek、Moonshot系列模型服务。
 *
 * @since 2.0.0
 */
public abstract class VolcesModelSpecification implements ModelSpecification {
    public static final String SPECIFICATION_NAME = "Volces";
    public static final String pathOfV3ChatCompletions = "/api/v3/chat/completions";
    public static final String hostOfV3ChatCompletions = "ark.cn-beijing.volces.com";

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.volces;
    }

    @Override
    public String getSpecificationName() {
        return SPECIFICATION_NAME;
    }

    /**
     * According to the sample `sample.config.properties`, here `config` is extracted from
     * {@code provider.volces}.
     *
     * @param config 针对提供服务的大语言模型在线服务提供商和相关大语言模型服务接口规格进行大语言模型服务适配器构建所需的配置
     */
    @Override
    public ServiceAdapter buildServiceAdapter(KeelConfigElement config) {
        String apiKey = config.readString(List.of("apiKey"));
        Map<String, String> modelDeploymentMap = new HashMap<>();
        KeelConfigElement models = config.extract("model");
        if (models != null) {
            models.getChildren().forEach((k, v) -> {
                modelDeploymentMap.put(k, v.getValueAsString());
            });
        }
        return new VolcesServiceAdapter(apiKey, modelDeploymentMap);
    }
}
