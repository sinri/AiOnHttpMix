package io.github.sinri.AiOnHttpMix.utils.providers;

import io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification;
import io.github.sinri.keel.facade.configuration.KeelConfigElement;

/**
 * 本接口定义了大语言模型在线服务提供商。一个大语言模型在线服务提供商通常会提供若干种大语言模型系列的调用接口服务。
 * 这个接口的实现类均应在本package下并实现构造函数为package-protected，然后在这个接口里放置静态单例。
 *
 * @since 1.4.0
 */
public interface ServiceProvider {
    DashscopeServiceProvider dashscope = new DashscopeServiceProvider();
    AzureOpenAIServiceProvider azureOpenAI = new AzureOpenAIServiceProvider();
    VolcesServiceProvider volces = new VolcesServiceProvider();

    String getProviderName();
}
