package io.github.sinri.AiOnHttpMix.utils.providers;

/**
 * 大语言模型在线服务提供商：字节跳动旗下的火山引擎的火山方舟（Volces）
 *
 * @since 2.0.0
 */
public class VolcesServiceProvider implements ServiceProvider {
    public static final String PROVIDER_NAME = "Volces";

    VolcesServiceProvider() {

    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }


}
