package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.deepseek;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen.AbstractQwenKitUnitTest;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;

public class DeepSeekChatOnDashScopeWithoutFCUnitTest extends AbstractQwenKitUnitTest
        implements DeepSeekOnDashScopeMixin {

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithoutToolCall() {
        AigcMix.enableVerboseLogger();
        this.async(() -> getKit()
                .chatForMessageResponse(getServiceMeta(), generateRequest(), generateRequestId())
                .compose(resp -> {
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = resp.getOutput()
                                                                                                    .getChoices();
                    assert choices != null;
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    getUnitTestLogger().debug("the first choice in response is ", choice.cloneAsJsonObject());
                    QwenMessage message = choice.getMessage();
                    //getUnitTestLogger().info("reasoning_content: " + message.getReasoningContent());
                    getUnitTestLogger().info("content: " + message.getContent());
                    return Future.succeededFuture();
                }));
        AigcMix.disableVerboseLogger();
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithChunkHandler(
                        getServiceMeta(),
                        generateRequest().handleParameters(p -> p.setIncrementalOutput(true)),
                        chunk -> {
                            QwenResponseChunk.OutputChunkForMessageResponse output = chunk.getOutput();
                            if (output != null) {
                                List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
                                if (choices != null) {
                                    QwenResponseChunk.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                                    QwenMessage message = choice.getMessage();
                                    QwenRole role = message.getRole();
                                    String content = message.getContent();
                                    getUnitTestLogger().info("From " + role + ": " + content);
                                }
                            }
                        },
                        0,
                        generateRequestId()
                )
        );
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamBufferWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithBuffer(getServiceMeta(), generateRequest(), 0, generateRequestId())
                .compose(resp -> {
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = resp.getOutput()
                                                                                                    .getChoices();
                    assert choices != null;
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    getUnitTestLogger().debug("the first choice in response is ", choice.cloneAsJsonObject());
                    QwenMessage message = choice.getMessage();
                    //getUnitTestLogger().info("reasoning_content: " + message.getReasoningContent());
                    getUnitTestLogger().info("content: " + message.getContent());
                    return Future.succeededFuture();
                }));
    }

    @Override
    public String getDeepSeekModelNameInDashScope() {
        return "deepseek-v3";
    }
}
