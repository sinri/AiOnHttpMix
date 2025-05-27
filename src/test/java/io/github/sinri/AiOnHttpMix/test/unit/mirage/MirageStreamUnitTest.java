package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.mix.chat.message.MixChatMessage;
import io.github.sinri.AiOnHttpMix.mix.service.SupportedModelEnum;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import org.junit.Test;

public class MirageStreamUnitTest extends AbstractMirageUnitTest {
    @Test
    public void test1() {
        AigcMix.enableVerboseLogger();
        async(() -> {
            MirageRequestEntity requestEntity = new MirageRequestEntity();
            requestEntity
                    .write("prompt", new JsonArray()
                            .add(MixChatMessage.create()
                                               .setRole("user")
                                               .setTextContent("介绍一下上野公园")
                                               .toJsonObject()
                            )
                    );

            return getMirageKit().requestStream(
                                         SupportedModelEnum.ChatGPT4o.name(),
                                         true,
                                         requestEntity,
                                         s -> {
                                             getUnitTestLogger().info("fragment: \n" + s);
                                             return Future.succeededFuture();
                                         }
                                 )
                                 .compose(resp -> {
                                     getUnitTestLogger().info("fin");
                                     return Future.succeededFuture();
                                 });
        });
    }
}
