package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenModel;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForNonFC;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QwenKitChatWithoutFCUnitTest extends AbstractQwenKitUnitTest
        implements LLMUnitTestCoverageForNonFC<QwenRequest> {

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithoutToolCall() {
        //        AigcMix.enableVerboseLogger();
        this.async(() -> getKit()
                .chatForMessageResponse(getServiceMeta(), generateRequest(), generateRequestId())
                .compose(resp -> {
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = resp.getOutput()
                                                                                                    .getChoices();
                    assert choices != null;
                    Assert.assertFalse(choices.isEmpty());
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    QwenMessage message = choice.getMessage();
                    QwenRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);
                    return Future.succeededFuture();
                })
        );
        //        AigcMix.disableVerboseLogger();
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamWithoutToolCall() {
        this.async(() -> getKit().chatStreamWithChunkHandler(
                getServiceMeta(),
                generateRequest().handleParameters(p -> p.setIncrementalOutput(true)),
                chunk -> {
                    List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = chunk.getOutput()
                                                                                                .getChoices();
                    if (choices != null && !choices.isEmpty()) {
                        QwenMessage message = choices.get(0).getMessage();
                        QwenRole role = message.getRole();
                        String content = message.getContent();
                        getUnitTestLogger().info("From " + role + ": " + content);
                    }
                },
                0,
                generateRequestId()
        ));
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamBufferWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithBuffer(
                        getServiceMeta(),
                        generateRequest(),
                        0,
                        generateRequestId()
                )
                .compose(resp -> {
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = resp.getOutput()
                                                                                                    .getChoices();
                    assert choices != null;
                    Assert.assertFalse(choices.isEmpty());
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    QwenMessage message = choice.getMessage();
                    QwenRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);
                    return Future.succeededFuture();
                })
        );
    }

    @Override
    public QwenRequest generateRequest() {
        return QwenRequest.create()
                          .setModel(QwenModel.QWEN_PLUS.getModelCode())
                          .handleInput(input -> input
                                  .addSystemMessage("你是李白，对，唐朝那个有名的。")
                                  .addUserMessage("大唐天下，商贾之子无法科举为官，如何才能出将入相？真不行的话不要逃避现实。")
                          )
                //.handleParameters(p -> p.setIncrementalOutput(true))
                ;
    }
}
