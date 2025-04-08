package io.github.sinri.AiOnHttpMix.test.unit.anyllm.mirage;

import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

public class AnyLLMMirageVolcesDoubaoUnitTest extends AbstractAnyLLMMirageUnitTest {
    @Override
    public AnyLLMKit createAnyLLMKit() {
        return new AnyLLMKit().useMirageSDK(generateMirageSDK(), SupportedModel.Doubao);
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
