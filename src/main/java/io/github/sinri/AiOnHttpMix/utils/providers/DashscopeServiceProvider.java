package io.github.sinri.AiOnHttpMix.utils.providers;

/**
 * 大语言模型在线服务提供商：阿里巴巴旗下阿里云的模搭（Dashscope）
 *
 * @since 2.0.0
 */
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
