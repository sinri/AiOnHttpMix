package io.github.sinri.AiOnHttpMix.utils.providers;

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


}
