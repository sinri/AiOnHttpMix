package io.github.sinri.AiOnHttpMix.test.unit.anyllm.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

public class AnyLLMMirageDeepSeekUnitTest extends AbstractAnyLLMMirageUnitTest {
    @Override
    public AnyLLMKit createAnyLLMKit() {
        return new AnyLLMKit(AnyLLMServiceAdapter.throughMirage(generateMirageSDK(), SupportedModel.DeepSeekChat));
    }

    @Test
    @Override
    public void testSyncWithoutToolCall() {
        super.testSyncWithoutToolCall();
    }

    @Test
    @Override
    public void testStreamBufferWithoutToolCall() {
        AigcMix.enableVerboseLogger();
        super.testStreamBufferWithoutToolCall();
        AigcMix.disableVerboseLogger();
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
