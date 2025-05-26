package io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.text;

import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.mixin.chat.MixTextStreamNonFCMixin;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.AbstractMixNativeUnitTest;
import org.junit.Test;

public class MixNativeTextStreamNonFCUnitTest extends AbstractMixNativeUnitTest implements MixTextStreamNonFCMixin {


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
