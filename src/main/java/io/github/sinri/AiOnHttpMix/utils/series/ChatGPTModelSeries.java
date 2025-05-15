package io.github.sinri.AiOnHttpMix.utils.series;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.ChatGPTServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import java.util.List;

public final class ChatGPTModelSeries implements ChatModelSeries {
    public static final String NAME = "ChatGPT";

    ChatGPTModelSeries() {
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.azureOpenAI;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ChatModelServiceAdapter buildServiceMeta(KeelConfigElement config) {
        String apiKey = config.readString(List.of("apiKey"));
        String resourceName = config.readString(List.of("resourceName"));
        String deployment = config.readString(List.of("deployment"));
        String apiVersion = config.readString(List.of("apiVersion"));
        return new ChatGPTServiceAdapter(apiKey, resourceName, deployment, apiVersion);
    }
}
