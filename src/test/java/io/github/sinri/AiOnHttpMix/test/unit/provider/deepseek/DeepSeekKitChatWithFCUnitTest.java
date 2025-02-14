package io.github.sinri.AiOnHttpMix.test.unit.provider.deepseek;

import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.chunk.DeepseekResponseChunk;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInRequest;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekRole;
import io.github.sinri.AiOnHttpMix.deepseek.core.DeepseekModel;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForFC;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DeepSeekKitChatWithFCUnitTest extends AbstractDeepSeekKitUnitTest
        implements LLMUnitTestCoverageForFC<DeepseekChatRequest> {
    @Override
    public void setUp() {
        super.setUp();
        //AigcMix.enableVerboseLogger();
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithToolCall() {
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

                    List<DeepseekMessageInResponse.DeepseekToolCallInResponse> toolCalls = message.getToolCalls();
                    assert toolCalls != null;
                    Assert.assertFalse(toolCalls.isEmpty());
                    DeepseekMessageInResponse.DeepseekToolCallInResponse toolCallInResponse = toolCalls.get(0);
                    DeepseekMessageInResponse.DeepseekToolCallInResponse.FunctionCall function = toolCallInResponse.getFunction();
                    getUnitTestLogger().info("Function " + function.getName() + "(" + function.getArguments() + ")");

                    return Future.succeededFuture();
                })
        );
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamWithToolCall() {
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

                    List<DeepseekResponseChunk.ChoiceChunkDeltaToolCall> toolCalls = delta.getToolCalls();
                    if (toolCalls != null) {
                        Assert.assertFalse(toolCalls.isEmpty());
                        DeepseekResponseChunk.ChoiceChunkDeltaToolCall toolCall = toolCalls.get(0);
                        var function = toolCall.getFunction();
                        assert function != null;
                        getUnitTestLogger().info("Function " + function.getName() + "(" + function.getArguments() + ")");
                    }
                }, 0, generateRequestId()));
    }


    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamBufferWithToolCall() {
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

                    List<DeepseekMessageInResponse.DeepseekToolCallInResponse> toolCalls = message.getToolCalls();
                    assert toolCalls != null;
                    Assert.assertFalse(toolCalls.isEmpty());
                    DeepseekMessageInResponse.DeepseekToolCallInResponse toolCallInResponse = toolCalls.get(0);
                    DeepseekMessageInResponse.DeepseekToolCallInResponse.FunctionCall function = toolCallInResponse.getFunction();
                    getUnitTestLogger().info("Function " + function.getName() + "(" + function.getArguments() + ")");

                    return Future.succeededFuture();
                })
        );
    }

    @Override
    public DeepseekChatRequest generateRequest() {
        var chatRequestFC = DeepseekChatRequest.create();
        chatRequestFC.setModel(DeepseekModel.ChatModel)
                     .addTool(new DeepseekChatRequest.ToolDefinition.Builder()
                             .functionName("weather_query")
                             .functionDescription("Query weather for a city on certain date.")
                             .propertyAsString("city", "The name of a city")
                             .propertyAsString("date", "The date in YYYY-MM-DD format.")
                             .build()
                     )
                     .addMessage(DeepseekMessageInRequest.create()
                                                         .setRole(DeepseekRole.system)
                                                         .setContent("你是情报局的人")
                     )
                     .addMessage(DeepseekMessageInRequest.create()
                                                         .setRole(DeepseekRole.user)
                                                         .setContent("明天要去杭州的深度求索公司里调查，需不需要带伞？")
                     );
        return chatRequestFC;
    }
}
