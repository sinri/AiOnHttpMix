package io.github.sinri.AiOnHttpMix.utils.providers;


import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.ChatGPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.specification.GPTModelSpecification;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

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
        if (modelSpecification instanceof GPTModelSpecification) {
            String apiKey = config.readString(List.of("apiKey"));
            String resourceName = config.readString(List.of("resourceName"));
            String deployment = config.readString(List.of("deployment"));
            String apiVersion = config.readString(List.of("apiVersion"));
            return new ChatGPTServiceAdapter(apiKey, resourceName, deployment, apiVersion);
        } else {
            throw new RuntimeException("modelSpecification is not supported");
        }
    }
}
