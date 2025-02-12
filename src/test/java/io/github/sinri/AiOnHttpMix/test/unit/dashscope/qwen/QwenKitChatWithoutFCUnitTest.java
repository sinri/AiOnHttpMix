package io.github.sinri.AiOnHttpMix.test.unit.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenModel;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.test.unit.LLMUnitTestCoverageForNonFC;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QwenKitChatWithoutFCUnitTest extends AbstractQwenKitUnitTest
        implements LLMUnitTestCoverageForNonFC<QwenRequest> {

    @Test
    @Override
    public void testSyncWithoutToolCall() {
        // todo check test failure
        AigcMix.enableVerboseLogger();
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
        AigcMix.disableVerboseLogger();
    }

    @Test
    @Override
    public void testStreamWithoutToolCall() {
        this.async(() -> getKit().chatStreamWithChunkHandler(
                getServiceMeta(),
                generateRequest(),
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
    @Override
    public void testStreamBufferWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithBuffer(getServiceMeta(), generateRequest(), 0, generateRequestId())
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
                                  .addUserMessage("大唐天下，商贾之子无法科举为官，如何才能出将入相？")
                          )
                //.handleParameters(p -> p.setIncrementalOutput(true))
                ;
    }
}
