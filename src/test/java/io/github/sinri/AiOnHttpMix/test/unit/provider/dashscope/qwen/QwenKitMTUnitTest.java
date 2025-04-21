package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.UUID;

public class QwenKitMTUnitTest extends AbstractQwenKitUnitTest {

    @Test
    public void testTranslateJP2CN() {
        async(() -> {
            return getKit().chatForMessageResponse(
                                   getServiceMeta(),
                                   req -> {
                                       req.setModel("qwen-mt-turbo");
                                       req.handleInput(input -> input
                                               .addUserMessage("周囲に凄惨（>せいさん>）な僧兵の死体がなければ、戦場映画のように映える。だが、残念なことに彼らの周りは血と臓物が飛び散っていた。"));
                                       req.handleParameters(p -> {
                                           p.setTranslationOptions(QwenRequest.Parameters.TranslationOptions.create()
                                                                                                            .setSourceLang(QwenRequest.Parameters.TranslationOptions.Language.JAPANESE)
                                                                                                            .setTargetLang(QwenRequest.Parameters.TranslationOptions.Language.CHINESE)
                                           );
                                       });
                                   },
                                   UUID.randomUUID().toString()
                           )
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());
                               return Future.succeededFuture();
                           });
        });
    }
}
