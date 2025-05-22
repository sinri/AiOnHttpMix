package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.vision;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInVisionRequest;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision.Content;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision.ContentImageUrl;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.request.DoubaoRequest;
import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.doubao.thinking.AbstractDoubaoThinkingModelUnitTest;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class DoubaoVisionLowLevelUnitTest extends AbstractDoubaoVisionModelUnitTest {

    private JsonObject generateRequest(boolean useStreamIncrement) {
        var imageUrl = "https://p9-arcosite.byteimg.com/tos-cn-i-goo7wpa0wc/f9a8b28c25ac4d86979de8f52e73def6~tplv-goo7wpa0wc-image.image";

        DoubaoRequest request = DoubaoRequest.create()
                                             //.addMessage(DoubaoMessageInChatRequest.createAsSystemMessage())
                                             .addMessage(DoubaoMessageInVisionRequest.createAsUserMessage(
                                                     List.of(
                                                             Content.create()
                                                                    .setType("text")
                                                                    .setText("这个图片里的动物是啥啊"),
                                                             Content.create()
                                                                    .setType("image_url")
                                                                    .setImageUrl(ContentImageUrl.create()
                                                                                                .setUrl(imageUrl)
                                                                    )
                                                     )
                                             ))
                .stream(useStreamIncrement);
        return request.toJsonObject();
    }

    @Test
    public void test1() {
        async(() -> {
            return buildServiceAdapter()
                    .request(
                            getModel(),
                            generateRequest(false),
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        getUnitTestLogger().info("resp", resp);
                        return Future.succeededFuture();
                    });
        });
    }

    @Test
    public void test2() {
        async(() -> {
            return buildServiceAdapter()
                    .requestStream(
                            getModel(),
                            generateRequest(true),
                            chunk -> {
                                getUnitTestLogger().info("chunk: " + chunk);
                                return Future.succeededFuture();
                            },
                            180_000L,
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        getUnitTestLogger().info("fin");
                        return Future.succeededFuture();
                    });
        });
    }
}
