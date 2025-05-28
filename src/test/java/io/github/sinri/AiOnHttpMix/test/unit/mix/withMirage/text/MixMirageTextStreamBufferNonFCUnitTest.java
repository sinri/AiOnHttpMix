package io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage.text;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.chat.MixTextStreamBufferNonFCMixin;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage.AbstractMixMirageUnitTest;
import org.junit.Test;

public class MixMirageTextStreamBufferNonFCUnitTest extends AbstractMixMirageUnitTest implements MixTextStreamBufferNonFCMixin {

    public MixMirageTextStreamBufferNonFCUnitTest() {
        super();
        AigcMix.enableVerboseLogger();
    }

    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.Doubao1d5ThinkingPro250415));
    }

    @Test
    public void testSyncSimpleForVolcesDeepSeek() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.DeepSeekChatOnVolces));
    }

    @Test
    public void testSyncSimpleForVolcesMoonshot() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.KimiOnVolces));
    }

    @Test
    public void testSyncSimpleForQwen() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.QwenPlusLatest));
    }
}
