package io.github.sinri.AiOnHttpMix.test.unit.anyllm.pure;

import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AnyLLMPureVolcesKimiUnitTest extends AbstractAnyLLMPureUnitTest<VolcesServiceMeta> {
    private String getServiceName() {
        return "moonshot-v1-128k";
    }

    @Override
    protected VolcesServiceMeta generateServiceMeta() {
        String serviceName = getServiceName();
        String apiKey = Keel.config("volces." + serviceName + ".apiKey");
        String model = Keel.config("volces." + serviceName + ".model");

        assert apiKey != null;
        assert model != null;
        return new VolcesServiceMeta(apiKey, model);
    }

    @Override
    public AnyLLMKit createAnyLLMKit() {
        return new AnyLLMKit().useServiceMeta(generateServiceMeta(), SupportedModel.KimiOnVolces);
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
