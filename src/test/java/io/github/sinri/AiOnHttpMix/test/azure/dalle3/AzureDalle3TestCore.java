package io.github.sinri.AiOnHttpMix.test.azure.dalle3;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.test.BaseUnitTest;
import org.junit.Before;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AzureDalle3TestCore extends BaseUnitTest {
    private String apiKey;
    private String resourceName;
    private String deployment;
    private String apiVersion;
    private AzureOpenAIServiceMeta serviceMeta;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        var serviceName = "dalle3";

        this.apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
        this.resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
        this.deployment = Keel.config("azure.openai." + serviceName + ".deployment");
        this.apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

        this.serviceMeta = new AzureOpenAIServiceMeta(apiKey, resourceName, deployment, apiVersion);
    }

    protected AzureOpenAIServiceMeta getServiceMeta() {
        return serviceMeta;
    }
}
