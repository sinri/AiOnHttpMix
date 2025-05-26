package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.gpt;

import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.GPTMessageInVisionRequest;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContent;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message.vision.GPTVisionMessageContentImageUrl;
import io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request.GPTRequest;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class GPTVisionStreamUnitTest extends AbstractGptUnitTest {
    @Test
    public void test1() {
        async(() -> {
            String imageUrl = "https://mmbiz.qpic.cn/sz_mmbiz_jpg/MWpJc0Ao1ic034EWO3nPkVh12H6Y44vjekM7JUBOyYKViartbR9TbUvB4KgxpXJ7O8G7HdDHpiaibATCqLHmJe6IPA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1";
            GPTRequest request = GPTRequest.create()
                                           .addMessage(GPTMessageInVisionRequest.createAsUser(List.of(
                                                           GPTVisionMessageContent.create()
                                                                                  .setImageUrl(GPTVisionMessageContentImageUrl.create()
                                                                                                                              .setUrl(imageUrl)
                                                                                  ),
                                                           GPTVisionMessageContent.create()
                                                                                  .setText("图中一共有几个人")
                                                   ))
                                           );

            return getKit()
                    .chatStream(
                            getModel(),
                            request,
                            180_000L,
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        getUnitTestLogger().info("resp", resp.cloneAsJsonObject());

                        GPTMessageInResponse message = resp.getChoices().get(0).getMessage();
                        getUnitTestLogger().info("role: " + message.getRole());
                        getUnitTestLogger().info("content: " + message.getContent());

                        return Future.succeededFuture();
                    });
        });
    }
}
