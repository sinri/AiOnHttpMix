package io.github.sinri.AiOnHttpMix.utils.providers;


import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.OModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.Map;
import java.util.stream.Collectors;

public final class AzureOpenAIServiceProvider implements ServiceProvider {
    public static final String PROVIDER_NAME = "AzureOpenAI";

    AzureOpenAIServiceProvider() {
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public ChatModelServiceAdapter buildServiceAdapter(ModelSpecification modelSpecification, KeelConfigElement config) {
        if (modelSpecification instanceof GPTModelSpecification
                || modelSpecification instanceof OModelSpecification
        ) {
            Map<String, OpenAIConfigElement> map = config.getChildren()
                                                         .entrySet()
                                                         .stream()
                                                         .collect(Collectors.toMap(
                                                                 Map.Entry::getKey,
                                                                 entry -> new OpenAIConfigElement(entry.getValue())
                                                         ));
            return new OpenAIServiceAdapter(map);
        } else {
            throw new RuntimeException("modelSpecification is not supported");
        }
    }
}
