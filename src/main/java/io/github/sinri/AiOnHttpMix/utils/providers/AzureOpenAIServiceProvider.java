package io.github.sinri.AiOnHttpMix.utils.providers;


import io.github.sinri.AiOnHttpMix.provider.azure.openai.OpenAIConfigElement;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.GPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.o.OServiceAdapter;
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
}
