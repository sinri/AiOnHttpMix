package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.vertx.core.Future;
import org.junit.Test;

public class MirageSyncNonFCUnitTest extends AbstractMirageUnitTest {
    public MirageSyncNonFCUnitTest(){
        super();
        AigcMix.enableVerboseLogger();
    }

    private MixChatRequest buildMirageRequestEntity(SupportedModelEnum supportedModelEnum) {
        MixChatRequest request = MixChatRequest.create(supportedModelEnum);
        request.addMessage(MixChatMessage.create()
                                         .setRole("user")
                                         .setTextContent("介绍一下上野公园")
        );
        return request;
    }

    private Future<Void> act(SupportedModelEnum supportedModelEnum) {
        return getMirageKit()
                .requestSync(true, buildMirageRequestEntity(supportedModelEnum))
                .compose(resp -> {
                    getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                    MixChatMessage message = resp.getMessage();
                    String role = message.getRole();
                    String textContent = message.getTextContent();
                    getUnitTestLogger().info(role + ":" + textContent);

                    return Future.succeededFuture();
                });
    }

    @Test
    public void test1() {
        async(() -> act(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void test2() {
        async(() -> act(SupportedModelEnum.QwenPlusLatest));
    }

    /**
     * Model `SupportedModelEnum.Doubao1d5ThinkingPro250415` is slow in sync mode.
     */
    @Test
    public void test3() {
        async(() -> act(SupportedModelEnum.Doubao1d5ThinkingPro250415));
    }

    @Test
    public void test4() {
        async(() -> act(SupportedModelEnum.DeepSeekChatOnVolces));
    }
}
