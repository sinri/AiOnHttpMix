package io.github.sinri.AiOnHttpMix.utils.specification;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Override
    public ChatModelServiceAdapter buildServiceAdapter(KeelConfigElement config) {
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
