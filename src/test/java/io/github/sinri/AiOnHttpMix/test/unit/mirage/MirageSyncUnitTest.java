package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import org.junit.Test;

public class MirageSyncUnitTest extends AbstractMirageUnitTest {
    @Test
    public void test1() {
        AigcMix.enableVerboseLogger();
        async(() -> {
            MirageRequestEntity requestEntity = new MirageRequestEntity();
            requestEntity
                    .write("prompt", new JsonArray()
                            .add(MixChatMessage.create()
                                               .setRole("user")
                                               .setTextContent("介绍希特勒的恶行")
                                               .toJsonObject()
                            )
                    );

            return getMirageKit().requestSync(
                                         SupportedModelEnum.ChatGPT4o.name(),
                                         true,
                                         requestEntity
                                 )
                                 .compose(resp -> {
                                     getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                                     MixChatMessage message = resp.getMessage();
                                     String role = message.getRole();
                                     String textContent = message.getTextContent();
                                     getUnitTestLogger().info(role + ":" + textContent);

                                     return Future.succeededFuture();
                                 });
        });
    }
}
