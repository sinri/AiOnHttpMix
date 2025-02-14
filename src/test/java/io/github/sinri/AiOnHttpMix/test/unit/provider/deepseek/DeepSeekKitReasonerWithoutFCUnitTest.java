package io.github.sinri.AiOnHttpMix.test.unit.provider.deepseek;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunk;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekRole;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekModel;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForNonFC;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestFailed;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;

public class DeepSeekKitReasonerWithoutFCUnitTest extends AbstractDeepSeekKitUnitTest
        implements LLMUnitTestCoverageForNonFC<DeepseekChatRequest> {
    /**
     * This test related API of DeepSeek is not stable now.
     */
    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithoutToolCall() {
        AigcMix.enableVerboseLogger();
        this.async(() -> getKit()
                .chat(getServiceMeta(), generateRequest(), generateRequestId())
                .compose(resp -> {
                    List<DeepseekChatResponse.Choice> choices = resp.getChoices();
                    DeepseekChatResponse.Choice choice = choices.get(0);
                    DeepseekMessageInResponse message = choice.getMessage();
                    DeepseekRole role = message.getRole();
                    String reasoningContent = message.getReasoningContent();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + " | " + reasoningContent + " | " + content);
                    return Future.succeededFuture();
                })
        );
        AigcMix.disableVerboseLogger();
    }

    /**
     * This test related API of DeepSeek is not stable now.
     */
    @Test
    @TestFailed(time = "2025-02-13", note = "DeepSeek Resource Not Enough")
    @Override
    public void testStreamWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithChunkHandler(getServiceMeta(), generateRequest(), chunk -> {
                    List<DeepseekResponseChunk.ChoiceChunk> choices = chunk.getChoices();
                    DeepseekResponseChunk.ChoiceChunk choiceChunk = choices.get(0);
                    DeepseekResponseChunk.ChoiceChunkDelta delta = choiceChunk.getDelta();
                    assert delta != null;
                    String role = delta.getRole();
                    String reasoningContent = delta.getReasoningContent();
                    String content = delta.getContent();
                    getUnitTestLogger().info("From " + role + " | " + reasoningContent + " | " + content);
                }, 0, generateRequestId()));
    }

    /**
     * This test related API of DeepSeek is not stable now.
     */
    @Test
    @TestFailed(time = "2025-02-13", note = "DeepSeek Resource Not Enough")
    @Override
    public void testStreamBufferWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithBuffer(getServiceMeta(), generateRequest(), 0, generateRequestId())
                .compose(resp -> {
                    List<DeepseekChatResponse.Choice> choices = resp.getChoices();
                    DeepseekChatResponse.Choice choice = choices.get(0);
                    DeepseekMessageInResponse message = choice.getMessage();
                    DeepseekRole role = message.getRole();
                    String reasoningContent = message.getReasoningContent();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + " | " + reasoningContent + " | " + content);
                    return Future.succeededFuture();
                })
        );
    }

    @Override
    public DeepseekChatRequest generateRequest() {
        var chatRequest = DeepseekChatRequest.create();
        chatRequest
                .setModel(DeepseekModel.ReasonerModel)
                .addMessage(DeepseekMessageInRequest.create()
                                                    .setRole(DeepseekRole.system)
                                                    .setContent("你是一个专业的Java开发者")
                )
                .addMessage(DeepseekMessageInRequest.create()
                                                    .setRole(DeepseekRole.user)
                                                    .setContent("有一个项目，里面会设定定时任务，其中某些任务会有突发高内存占用，项目采用ZGC，请给点让项目不要老是挂掉的建议")
                );
        return chatRequest;
    }
}
