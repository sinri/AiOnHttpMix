package io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage.vision;

import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.vision.MixVisionSyncFCMixin;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage.AbstractMixMirageUnitTest;
import org.junit.Test;

public class MixMirageVisionSyncFCUnitTest extends AbstractMixMirageUnitTest implements MixVisionSyncFCMixin {
    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestSync(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        async(() -> toTestSync(SupportedModelEnum.Doubao1d5VisionPro32k250115));
    }

    public void testSyncSimpleForVolcesDeepSeek() {
        async(() -> toTestSync(SupportedModelEnum.DeepSeekChatOnVolces));
    }

    public void testSyncSimpleForVolcesMoonshot() {
        async(() -> toTestSync(SupportedModelEnum.KimiOnVolces));
    }

    @Test
    public void testSyncSimpleForQwen() {

        async(() -> toTestSync(SupportedModelEnum.QwenVLPlusLatest));
    }
}
