package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForNonFC;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.UUID;

public class QwenKitChatWithThinkTest extends AbstractQwenKitUnitTest
        implements LLMUnitTestCoverageForNonFC<QwenRequest> {
    @Override
    public void testSyncWithoutToolCall() {
        // 仅支持增量流式返回，本测试不成立
    }

    @Test
    @Override
    public void testStreamWithoutToolCall() {
        async(() -> {
            return getKit()
                    .chatStreamWithChunkHandler(
                            getServiceMeta(),
                            generateRequest(),
                            chunk -> {
                                QwenMessage message = chunk.getOutput().getChoices().get(0).getMessage();
                                getUnitTestLogger().info("chunk message", message.toJsonObject());
                            },
                            180,
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        getUnitTestLogger().info("chunk end");
                        return Future.succeededFuture();
                    });
        });
    }

    @Test
    @Override
    public void testStreamBufferWithoutToolCall() {
        async(() -> {
            return getKit()
                    .chatStreamWithBuffer(
                            getServiceMeta(),
                            generateRequest(),
                            180,
                            UUID.randomUUID().toString()
                    )
                    .compose(resp -> {
                        QwenMessage message = resp.getOutput().getChoices().get(0).getMessage();
                        getUnitTestLogger().info("resp message", message.toJsonObject());
                        return Future.succeededFuture();
                    });
        });
    }

    @Override
    public QwenRequest generateRequest() {
        return QwenRequest.create()
                          .setModel("qwen-plus-latest")
                          .handleParameters(p -> p
                                  .setEnableThinking(true)
                                  .setIncrementalOutput(true)
                          )
                          .handleInput(input -> input
                                  .addSystemMessage("你是一个电商行业的资深专家")
                                  .addUserMessage("如何在长时间周期里唯一识别一家淘系店铺？")
                          );
    }
}
