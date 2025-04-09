package io.github.sinri.AiOnHttpMix.test.unit.anyllm.pure;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMServiceAdapter;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AnyLLMPureDashScopeQwenUnitTest extends AbstractAnyLLMPureUnitTest<DashscopeServiceMeta> {
    @Override
    protected DashscopeServiceMeta generateServiceMeta() {
        String dashscopeApiKey = Keel.config("dashscope.api_key");
        return new DashscopeServiceMeta(dashscopeApiKey);
    }

    @Override
    public AnyLLMKit createAnyLLMKit() {
        return new AnyLLMKit(AnyLLMServiceAdapter.throughSDK(generateServiceMeta(), SupportedModel.QwenPlus));
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
