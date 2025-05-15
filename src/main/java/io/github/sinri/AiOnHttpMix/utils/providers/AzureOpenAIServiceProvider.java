package io.github.sinri.AiOnHttpMix.utils.providers;


public final class AzureOpenAIServiceProvider implements ServiceProvider {
    public static final String NAME = "AzureOpenAI";

    AzureOpenAIServiceProvider() {
    }

    @Override
    public String getName() {
        return NAME;
    }
}
