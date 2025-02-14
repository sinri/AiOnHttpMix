package io.github.sinri.AiOnHttpMix.test.unit.anyllm.pure;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AnyLLMPureVolcesDSRUnitTest extends AbstractAnyLLMPureUnitTest<VolcesServiceMeta> {
    private String getServiceName() {
        return "DeepSeek-R1";
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
        return new AnyLLMKit().useServiceMeta(generateServiceMeta(), SupportedModel.DeepSeekChatOnVolces);
    }

    @Test
    @Override
    public void testSyncWithoutToolCall() {
        AigcMix.enableVerboseLogger();
        super.testSyncWithoutToolCall();
        AigcMix.disableVerboseLogger();
    }

    @Test
    @Override
    public void testStreamBufferWithoutToolCall() {
        super.testStreamBufferWithoutToolCall();
    }

    @Test
    @Override
    public void testSyncWithToolCall() {
        //        super.testSyncWithToolCall();
    }

    @Test
    @Override
    public void testStreamBufferWithToolCall() {
        //        super.testStreamBufferWithToolCall();
    }
}
