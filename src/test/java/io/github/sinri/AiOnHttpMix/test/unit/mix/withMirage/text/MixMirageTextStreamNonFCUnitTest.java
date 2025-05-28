package io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage.text;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.chat.MixTextStreamNonFCMixin;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withMirage.AbstractMixMirageUnitTest;
import org.junit.Test;

public class MixMirageTextStreamNonFCUnitTest extends AbstractMixMirageUnitTest implements MixTextStreamNonFCMixin {
    public MixMirageTextStreamNonFCUnitTest() {
        super();
        AigcMix.enableVerboseLogger();
    }


    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestStream(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        async(() -> toTestStream(SupportedModelEnum.Doubao1d5ThinkingPro250415));
    }

    @Test
    public void testSyncSimpleForVolcesDeepSeek() {
        async(() -> toTestStream(SupportedModelEnum.DeepSeekChatOnVolces));
    }

    @Test
    public void testSyncSimpleForVolcesMoonshot() {
        async(() -> toTestStream(SupportedModelEnum.KimiOnVolces));
    }

    @Test
    public void testSyncSimpleForQwen() {
        async(() -> toTestStream(SupportedModelEnum.QwenPlusLatest));
    }
}
