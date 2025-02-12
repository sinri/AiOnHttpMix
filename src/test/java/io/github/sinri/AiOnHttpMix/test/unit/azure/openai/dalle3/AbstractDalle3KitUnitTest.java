package io.github.sinri.AiOnHttpMix.test.unit.azure.openai.dalle3;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.azure.openai.dalle.Dalle3Kit;
import io.github.sinri.AiOnHttpMix.test.unit.AnyUnitTest;
import org.junit.Before;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractDalle3KitUnitTest extends AnyUnitTest {
    private AzureOpenAIServiceMeta serviceMeta;
    private Dalle3Kit dalle3Kit;

    @Before
    public void setUp() {
        super.setUp();

        var serviceName = getServiceName();//"dalle3";

        var apiKey = Keel.config("azure.openai." + serviceName + ".apiKey");
        var resourceName = Keel.config("azure.openai." + serviceName + ".resourceName");
        var deployment = Keel.config("azure.openai." + serviceName + ".deployment");
        var apiVersion = Keel.config("azure.openai." + serviceName + ".apiVersion");

        assert apiKey != null;
        assert resourceName != null;
        assert deployment != null;
        assert apiVersion != null;
        this.serviceMeta = new AzureOpenAIServiceMeta(apiKey, resourceName, deployment, apiVersion);

        this.dalle3Kit = new Dalle3Kit();
    }

    abstract protected String getServiceName();

    protected AzureOpenAIServiceMeta getServiceMeta() {
        return serviceMeta;
    }

    public Dalle3Kit getDalle3Kit() {
        return dalle3Kit;
    }
}
