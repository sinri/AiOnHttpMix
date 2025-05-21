package io.github.sinri.AiOnHttpMix.utils.providers;

public class VolcesServiceProvider implements ServiceProvider {
    public static final String PROVIDER_NAME = "Volces";

    VolcesServiceProvider() {

    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }


}
