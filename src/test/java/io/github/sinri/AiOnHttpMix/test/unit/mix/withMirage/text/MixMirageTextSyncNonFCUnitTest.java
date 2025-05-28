package io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage.text;

import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.chat.MixTextSyncNonFCMixin;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage.AbstractMixMirageUnitTest;
import org.junit.Test;

public class MixMirageTextSyncNonFCUnitTest extends AbstractMixMirageUnitTest implements MixTextSyncNonFCMixin {

    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestSync(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        async(() -> toTestSync(SupportedModelEnum.Doubao1d5ThinkingPro250415));
    }

    @Test
    public void testSyncSimpleForVolcesDeepSeek() {
        async(() -> toTestSync(SupportedModelEnum.DeepSeekChatOnVolces));
    }

    @Test
    public void testSyncSimpleForVolcesMoonshot() {
        async(() -> toTestSync(SupportedModelEnum.KimiOnVolces));
    }

    @Test
    public void testSyncSimpleForQwen() {

        async(() -> toTestSync(SupportedModelEnum.QwenPlusLatest));
    }
}
