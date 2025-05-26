package io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.vision;

import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.vision.MixVisionStreamBufferFCMixin;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.AbstractMixNativeUnitTest;
import org.junit.Test;

public class MixNativeVisionStreamBufferFCUnitTest extends AbstractMixNativeUnitTest implements MixVisionStreamBufferFCMixin {

    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.Doubao1d5VisionPro32k250115));
    }

    public void testSyncSimpleForVolcesDeepSeek() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.DeepSeekChatOnVolces));
    }

    public void testSyncSimpleForVolcesMoonshot() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.KimiOnVolces));
    }

    @Test
    public void testSyncSimpleForQwen() {

        async(() -> toTestStreamBuffer(SupportedModelEnum.QwenVLPlusLatest));
    }
}
