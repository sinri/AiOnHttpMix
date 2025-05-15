package io.github.sinri.AiOnHttpMix.utils.series;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

public final class QwenModelSeries implements ChatModelSeries {
    public static final String NAME = "Qwen";

    QwenModelSeries() {

    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.dashscope;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ChatModelServiceAdapter buildServiceMeta(KeelConfigElement config) {
        String apiKey = config.readString(List.of("apiKey"));
        return new QwenServiceAdapter(apiKey);
    }
}
