package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.test.BaseUnitTest;
import io.github.sinri.keel.logger.KeelLogLevel;
import org.junit.Before;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AzureChatUnitTestCore extends BaseUnitTest {
    private AzureOpenAIServiceMeta serviceMeta;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        var serviceName = "gpt-4-o";

        String apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
        String resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
        String deployment = Keel.config("azure.openai." + serviceName + ".deployment");
        String apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

        this.serviceMeta = new AzureOpenAIServiceMeta(apiKey, resourceName, deployment, apiVersion);

        getLogger().setVisibleLevel(KeelLogLevel.DEBUG);
    }

    protected AzureOpenAIServiceMeta getServiceMeta() {
        return serviceMeta;
    }

}
