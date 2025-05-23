package io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.mix.chat.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.AbstractMixNativeUnitTest;
import io.vertx.core.Future;
import org.junit.Test;

public class MixNativeGPTStreamNonFCUnitTest extends AbstractMixNativeUnitTest {
    private Future<Void> toTestStream(SupportedModelEnum supportedModelEnum) {
        return getMixChatKit()
                .chatStream(
                        MixChatRequest.create()
                                      .setSupportedModelEnum(supportedModelEnum)
                                      .addMessage(MixChatMessage.create()
                                                                .setRole("user")
                                                                .setContent("文明VI里面埃里温城邦的加成是什么")
                                      ),
                        fragment -> {
                            getUnitTestLogger().info("fragment: \n" + fragment);
                            return Future.succeededFuture();
                        }
                )
                .compose(v -> {
                    getUnitTestLogger().info("fin");
                    return Future.succeededFuture();
                });
    }

    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestStream(SupportedModelEnum.ChatGPT));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        async(() -> toTestStream(SupportedModelEnum.Doubao));
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
