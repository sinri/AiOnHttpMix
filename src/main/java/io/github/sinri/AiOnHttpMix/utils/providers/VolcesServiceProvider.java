package io.github.sinri.AiOnHttpMix.utils.providers;

public class VolcesServiceProvider implements ServiceProvider {
    public static final String NAME = "Volces";

    VolcesServiceProvider() {

    }

    @Override
    public String getName() {
        return NAME;
    }
}
