package io.github.sinri.AiOnHttpMix.utils.providers;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.QwenModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

public final class DashscopeServiceProvider implements ServiceProvider {
    public static final String PROVIDER_NAME = "Dashscope";
    public final static String hostOfDashscope = "dashscope.aliyuncs.com";
    public final static String pathOfDashscopeQwenTextGenerate = "/api/v1/services/aigc/text-generation/generation";
    public final static String endpointOfDashscopeQwenTextGenerate = "https://" + hostOfDashscope + pathOfDashscopeQwenTextGenerate;

    DashscopeServiceProvider() {
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public ChatModelServiceAdapter buildServiceAdapter(ModelSpecification modelSpecification, KeelConfigElement config) {
        if (modelSpecification instanceof QwenModelSpecification) {
            String apiKey = config.readString(List.of("apiKey"));
            return new QwenServiceAdapter(apiKey);
        } else {
            throw new RuntimeException("modelSpecification is not supported");
        }
    }
}
