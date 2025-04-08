package io.github.sinri.AiOnHttpMix.test.unit.anyllm.pure;

import io.github.sinri.AiOnHttpMix.azure.openai.core.AzureOpenAIServiceMeta;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AnyLLMPureAzureUnitTest extends AbstractAnyLLMPureUnitTest<AzureOpenAIServiceMeta> {
    public AnyLLMKit createAnyLLMKit() {
        return new AnyLLMKit().useServiceMeta(generateServiceMeta(), SupportedModel.ChatGPT);
    }

    private String getServiceName() {
        return "gpt-4-o";
    }

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

    @Test
    @Override
    public void testSyncWithoutToolCall() {
        super.testSyncWithoutToolCall();
    }

    @Test
    @Override
    public void testStreamBufferWithoutToolCall() {
        super.testStreamBufferWithoutToolCall();
    }

    @Test
    @Override
    public void testSyncWithToolCall() {
        super.testSyncWithToolCall();
    }

    @Test
    @Override
    public void testStreamBufferWithToolCall() {
        super.testStreamBufferWithToolCall();
    }
}
