package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.utils.models.ChatModel;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.UUID;

public class DoubaoSyncUnitTest extends AbstractDoubaoKitUnitTest {
    @Test
    public void test1() {
        async(() -> {
            DoubaoRequest request = DoubaoRequest.create()
                                                 .addMessage(DoubaoMessageInRequest.createAsSystemMessage("你是一个王心凌铁粉"))
                                                 .addMessage(DoubaoMessageInRequest.createAsUserMessage("王心凌的教育经历是什么"));

            return getKit().chat(
                                   getServiceAdapter(),
                                   ChatModel.doubaoPro32k,
                                   request,
                                   UUID.randomUUID().toString()
                           )
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                               return Future.succeededFuture();
                           });
        });
    }
}
