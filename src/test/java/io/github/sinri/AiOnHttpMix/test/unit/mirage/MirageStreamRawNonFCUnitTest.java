package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.chat.request.MixChatRequest;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.vertx.core.Future;
import org.junit.Test;

public class MirageStreamRawNonFCUnitTest extends AbstractMirageUnitTest {
    private MixChatRequest buildMirageRequestEntity(SupportedModelEnum supportedModelEnum) {
        MixChatRequest request = MixChatRequest.create(supportedModelEnum);
        request.addMessage(MixChatMessage.create()
                                         .setRole("user")
                                         .setTextContent("介绍一下上野公园")
        );
        return request;
    }

    private Future<Void> act(SupportedModelEnum supportedModelEnum) {
        return getMirageKit().requestStreamRaw(
                                     true,
                                     buildMirageRequestEntity(supportedModelEnum),
                                     fragmentData -> {
                                         getUnitTestLogger().info("fragment data: \n" + fragmentData);
                                         return Future.succeededFuture();
                                     }
                             )
                             .compose(resp -> {
                                 getUnitTestLogger().info("fin");
                                 return Future.succeededFuture();
                             });
    }

    @Test
    public void test1() {
        //        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void test2() {
        //        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.QwenPlusLatest));
    }

    /**
     * Model `SupportedModelEnum.Doubao1d5ThinkingPro250415` is slow in sync mode.
     */
    @Test
    public void test3() {
        //        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.Doubao1d5ThinkingPro250415));
    }

    @Test
    public void test4() {
        //        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.DeepSeekChatOnVolces));
    }
}
