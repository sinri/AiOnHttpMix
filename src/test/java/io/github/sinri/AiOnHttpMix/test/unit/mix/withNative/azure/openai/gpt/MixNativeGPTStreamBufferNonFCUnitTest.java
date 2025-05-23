package io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.mix.chat.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.github.sinri.AiOnHttpMix.test.unit.mix.withNative.AbstractMixNativeUnitTest;
import io.vertx.core.Future;
import org.junit.Test;

public class MixNativeGPTStreamBufferNonFCUnitTest extends AbstractMixNativeUnitTest {
    private Future<Void> toTestStreamBuffer(SupportedModelEnum supportedModelEnum) {
        return getMixChatKit()
                .chatStream(
                        MixChatRequest.create()
                                      .setSupportedModelEnum(supportedModelEnum)
                                      .addMessage(MixChatMessage.create()
                                                                .setRole("user")
                                                                .setContent("文明VI里面埃里温城邦的加成是什么")
                                      )
                )
                .compose(resp -> {
                    getUnitTestLogger().info("resp", resp.cloneAsJsonObject());
                    MixChatMessage message = resp.getMessage();
                    String role = message.getRole();
                    String content = message.getContent();
                    String reasoningContent = message.getReasoningContent();
                    getUnitTestLogger().info("role: " + role + "\nreasoning content: " + reasoningContent + "\ncontent: " + content);
                    return Future.succeededFuture();
                });
    }

    @Test
    public void testSyncSimpleForGPT() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.ChatGPT));
    }

    @Test
    public void testSyncSimpleForDoubao() {
        async(() -> toTestStreamBuffer(SupportedModelEnum.Doubao));
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
