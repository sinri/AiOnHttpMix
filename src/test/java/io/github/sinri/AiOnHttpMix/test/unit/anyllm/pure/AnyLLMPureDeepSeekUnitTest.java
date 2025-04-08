package io.github.sinri.AiOnHttpMix.test.unit.anyllm.pure;

import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekServiceMeta;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AnyLLMPureDeepSeekUnitTest extends AbstractAnyLLMPureUnitTest<DeepseekServiceMeta> {

    @Override
    protected DeepseekServiceMeta generateServiceMeta() {
        String apiKey = Keel.config("DeepSeek.main.apiKey");
        return new DeepseekServiceMeta(apiKey);
    }

    @Override
    public AnyLLMKit createAnyLLMKit() {
        return new AnyLLMKit().useServiceMeta(generateServiceMeta(), SupportedModel.DeepSeekReasoner);
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
