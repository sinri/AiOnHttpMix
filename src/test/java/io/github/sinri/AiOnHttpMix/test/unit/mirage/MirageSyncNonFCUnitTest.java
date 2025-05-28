package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import org.junit.Test;

public class MirageSyncNonFCUnitTest extends AbstractMirageUnitTest {

    private MirageRequestEntity buildMirageRequestEntity() {
        MirageRequestEntity requestEntity = new MirageRequestEntity();
        requestEntity
                .write("prompt", new JsonArray()
                        .add(MixChatMessage.create()
                                           .setRole("user")
                                           .setTextContent("介绍一下上野公园")
                                           .toJsonObject()
                        )
                );
        return requestEntity;
    }

    private Future<Void> act(SupportedModelEnum supportedModelEnum) {
        return getMirageKit().requestSync(
                                     supportedModelEnum.name(),
                                     true,
                                     buildMirageRequestEntity()
                             )
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
        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.ChatGPT4o));
    }

    @Test
    public void test2() {
        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.QwenPlusLatest));
    }

    /**
     * Model `SupportedModelEnum.Doubao1d5ThinkingPro250415` is slow in sync mode.
     */
    @Test
    public void test3() {
        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.Doubao1d5ThinkingPro250415));
    }

    @Test
    public void test4() {
        AigcMix.enableVerboseLogger();
        async(() -> act(SupportedModelEnum.DeepSeekChatOnVolces));
    }
}
