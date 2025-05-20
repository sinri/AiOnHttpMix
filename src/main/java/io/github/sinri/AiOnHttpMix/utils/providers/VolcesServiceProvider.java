package io.github.sinri.AiOnHttpMix.utils.providers;

import io.github.sinri.AiOnHttpMix.provider.volces.VolcesServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.specification.VolcesModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolcesServiceProvider implements ServiceProvider {
    public static final String PROVIDER_NAME = "Volces";

    VolcesServiceProvider() {

    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public ChatModelServiceAdapter buildServiceAdapter(ModelSpecification modelSpecification, KeelConfigElement config) {
        if (modelSpecification instanceof VolcesModelSpecification) {
            String apiKey = config.readString(List.of("apiKey"));
            Map<String, String> modelDeploymentMap = new HashMap<>();
            KeelConfigElement models = config.extract("model");
            if (models != null) {
                models.getChildren().forEach((k, v) -> {
                    modelDeploymentMap.put(k, v.getValueAsString());
                });
            }
            return new VolcesServiceAdapter(apiKey, modelDeploymentMap);
        } else {
            throw new RuntimeException("modelSpecification is not supported");
        }
    }
}
