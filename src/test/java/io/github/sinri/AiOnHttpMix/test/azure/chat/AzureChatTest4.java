package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Test for Azure with SSE query.
 */
public class AzureChatTest4 extends AzureChatTestCore {
    private OpenAIChatGptRequest parameters;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    parameters = OpenAIChatGptRequest.create()
                            .addTool(t -> t.functionName("searchDataSet")
                                    .functionDescription("根据信息查询可能的数据集")
                                    .propertyAsString("keywords", "由一组关键字字符串组成的JSON数组")
                            )
                            .addMessage(m -> m.system("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。"))
                            .addMessage(m -> m.user("每年在天猫平台上达成的商品销售额"));
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chatStream(
                        getServiceMeta(),
                        parameters.toJsonObject(),
                        s -> {
                            getLogger().info("ChunkString | " + s);
                        },
                        requestId);
    }
}
