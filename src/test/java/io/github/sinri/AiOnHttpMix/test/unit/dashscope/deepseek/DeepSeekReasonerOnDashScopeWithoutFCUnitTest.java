package io.github.sinri.AiOnHttpMix.test.unit.dashscope.deepseek;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.test.unit.dashscope.qwen.AbstractQwenKitUnitTest;
import io.vertx.core.Future;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DeepSeekReasonerOnDashScopeWithoutFCUnitTest extends AbstractQwenKitUnitTest
        implements DeepSeekOnDashScopeMixin {

    @Before
    @Override
    public void setUp() {
        super.setUp();
        AigcMix.enableVerboseLogger();
    }

    @Test
    @Override
    public void testSyncWithoutToolCall() {
        // todo check test failure: parameter incremental_output only support stream call
        this.async(() -> getKit()
                .chatForMessageResponse(getServiceMeta(), generateRequest(), generateRequestId())
                .compose(resp -> {
                    QwenResponseInMessageFormat.OutputForMessageResponse output = resp.getOutput();
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = output.getChoices();
                    assert choices != null;
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    getUnitTestLogger().debug("the first choice in response is ", choice.cloneAsJsonObject());
                    QwenMessage message = choice.getMessage();
                    getUnitTestLogger().info("reasoning_content: " + message.getReasoningContent());
                    getUnitTestLogger().info("content: " + message.getContent());
                    return Future.succeededFuture();
                }));
    }

    @Test
    @Override
    public void testStreamWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithChunkHandler(
                        getServiceMeta(),
                        generateRequest(),
                        chunk -> {
                            QwenResponseChunk.OutputChunkForMessageResponse output = chunk.getOutput();
                            if (output != null) {
                                List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
                                if (choices != null) {
                                    QwenResponseChunk.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                                    QwenMessage message = choice.getMessage();
                                    QwenRole role = message.getRole();
                                    String reasoningContent = message.getReasoningContent();
                                    String content = message.getContent();
                                    getUnitTestLogger().info("From " + role + " | " + reasoningContent + " || " + content);
                                }
                            }
                        },
                        0,
                        generateRequestId()
                )
        );
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
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    getUnitTestLogger().debug("the first choice in response is ", choice.cloneAsJsonObject());
                    QwenMessage message = choice.getMessage();
                    getUnitTestLogger().info("reasoning_content: " + message.getReasoningContent());
                    getUnitTestLogger().info("content: " + message.getContent());
                    return Future.succeededFuture();
                }));
    }

    @Override
    public String getDeepSeekModelNameInDashScope() {
        return "deepseek-r1";
    }
}
