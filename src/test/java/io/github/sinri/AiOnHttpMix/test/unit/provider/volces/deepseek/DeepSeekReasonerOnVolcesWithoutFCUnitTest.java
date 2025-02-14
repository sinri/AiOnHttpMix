package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.deepseek;

import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunk;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekRole;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForNonFC;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.github.sinri.AiOnHttpMix.test.unit.provider.volces.v3.AbstractVolcesKitTestUnit;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;

public class DeepSeekReasonerOnVolcesWithoutFCUnitTest extends AbstractVolcesKitTestUnit
        implements LLMUnitTestCoverageForNonFC<DeepseekChatRequest> {
    @Override
    protected String getServiceName() {
        return "DeepSeek-R1";
    }

    @Override
    public DeepseekChatRequest generateRequest() {
        return DeepseekChatRequest.create()
                                  .addMessage(DeepseekMessageInRequest.create()
                                                                      .setRole(DeepseekRole.system)
                                                                      .setContent("你是一个家庭社会学专家。")
                                  )
                                  .addMessage(DeepseekMessageInRequest.create()
                                                                      .setRole(DeepseekRole.user)
                                                                      .setContent("我准备开展一个单亲家庭条件下影响幼儿心理健全的因素的研究，请提供专业的课题实施方案建议。")
                                  );
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithoutToolCall() {
        this.async(() -> getKit()
                .chatForDeepSeekV3(getServiceMeta(), generateRequest(), generateRequestId())
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

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamWithoutToolCall() {
        this.async(() -> getKit()
                .chatStreamWithChunkHandlerForDeepSeekV3(getServiceMeta(), generateRequest(), chunk -> {
                    List<DeepseekResponseChunk.ChoiceChunk> choices = chunk.getChoices();
                    if (choices != null) {
                        DeepseekResponseChunk.ChoiceChunk choice = choices.get(0);
                        DeepseekResponseChunk.ChoiceChunkDelta delta = choice.getDelta();
                        if (delta != null) {
                            String role = delta.getRole();
                            String reasoningContent = delta.getReasoningContent();
                            String content = delta.getContent();
                            getUnitTestLogger().info("From " + role + " | " + reasoningContent + " | " + content);
                        }
                    }
                }, 0, generateRequestId())
        );
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamBufferWithoutToolCall() {
        this.async(() -> getKit()
                .chatSSEWithBufferForDeepSeekV3(getServiceMeta(), generateRequest(), 0, generateRequestId())
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
}
