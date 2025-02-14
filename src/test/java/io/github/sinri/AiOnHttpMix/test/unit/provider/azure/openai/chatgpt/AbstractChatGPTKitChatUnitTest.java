package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyKitUnitTest;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractChatGPTKitChatUnitTest
        extends AnyKitUnitTest<AzureOpenAIServiceMeta, ChatGPTKit> {

    abstract protected String getServiceName();

    @Override
    protected final AzureOpenAIServiceMeta generateServiceMeta() {
        var serviceName = getServiceName();

        String apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
        String resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
        String deployment = Keel.config("azure.openai." + serviceName + ".deployment");
        String apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

        assert apiKey != null;
        assert resourceName != null;
        assert deployment != null;
        assert apiVersion != null;
        return new AzureOpenAIServiceMeta(apiKey, resourceName, deployment, apiVersion);
    }

    @Override
    protected ChatGPTKit generateKit() {
        return new ChatGPTKit();
    }
}
