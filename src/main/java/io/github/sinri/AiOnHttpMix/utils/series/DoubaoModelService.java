package io.github.sinri.AiOnHttpMix.utils.series;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.DoubaoServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DoubaoModelService implements ChatModelSeries {
    public static final String NAME = "Doubao";
    public static final String pathOfV3ChatCompletions = "/api/v3/chat/completions";
    public static final String hostOfV3ChatCompletions = "ark.cn-beijing.volces.com";

    DoubaoModelService() {

    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.volces;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ChatModelServiceAdapter buildServiceMeta(KeelConfigElement config) {
        String apiKey = config.readString(List.of("apiKey"));
        Map<String, String> modelDeploymentMap = new HashMap<>();
        KeelConfigElement models = config.extract("model");
        if (models != null) {
            models.getChildren().forEach((k, v) -> {
                modelDeploymentMap.put(k, v.getValueAsString());
            });
        }
        return new DoubaoServiceAdapter(apiKey, modelDeploymentMap);
    }
}
