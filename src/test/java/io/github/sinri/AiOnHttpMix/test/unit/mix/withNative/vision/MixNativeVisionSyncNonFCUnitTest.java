package io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.vision;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.vision.MixVisionSyncNonFCMixin;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.AbstractMixNativeUnitTest;
import org.junit.Test;

public class MixNativeVisionSyncNonFCUnitTest extends AbstractMixNativeUnitTest implements MixVisionSyncNonFCMixin {
    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestSync(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        AigcMix.enableVerboseLogger();
        async(() -> toTestSync(SupportedModelEnum.Doubao1d5VisionPro32k250115));
    }

    /**
     * there is not a vision model of deepseek on volces
     */
    public void testSyncSimpleForVolcesDeepSeek() {
        async(() -> toTestSync(SupportedModelEnum.DeepSeekChatOnVolces));
    }

    /**
     * there is not a vision model of moonshot on volces
     */
    public void testSyncSimpleForVolcesMoonshot() {
        async(() -> toTestSync(SupportedModelEnum.KimiOnVolces));
    }

    @Test
    public void testSyncSimpleForQwen() {
        AigcMix.enableVerboseLogger();
        async(() -> toTestSync(SupportedModelEnum.QwenVLPlusLatest));
    }
}
