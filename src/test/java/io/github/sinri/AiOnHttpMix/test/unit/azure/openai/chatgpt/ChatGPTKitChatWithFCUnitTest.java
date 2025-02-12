package io.github.sinri.AiOnHttpMix.test.unit.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoiceDelta;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptToolDefinition;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.github.sinri.AiOnHttpMix.test.unit.LLMUnitTestCoverageForFC;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ChatGPTKitChatWithFCUnitTest extends AbstractChatGPTKitChatUnitTest
        implements LLMUnitTestCoverageForFC<OpenAIChatGptRequest> {
    @Override
    public OpenAIChatGptRequest generateRequest() {
        return OpenAIChatGptRequest.create()
                                   .addMessage(msg -> msg.system("你是杭州的一家公司的行政人员，负责安排高层人员行程。"))
                                   .addMessage(msg -> msg.user("2025年春节老板在泉州鲤城区西街有个考察访问活动，打算提前一天抵达，请安排行程并准备提醒事项。"))
                                   .addTool(OpenAIChatGptToolDefinition
                                           .builder()
                                           .functionName("travel_scheduler")
                                           .functionDescription("schedule travel schedule and traffic")
                                           .propertyAsString("from", "the city to set off")
                                           .propertyAsString("to", "the city as the destination")
                                           .propertyAsString("set_off_day", "the day to set off, in YYYY-MM-DD format")
                                           .build());
    }

    @Test
    @Override
    public void testSyncWithToolCall() {
        this.async(() -> getKit().chat(
                                         getServiceMeta(),
                                         generateRequest(),
                                         generateRequestId()
                                 )
                                 .compose(resp -> {
                                     OpenAIChatGptResponseChoice choice = resp.getChoices().get(0);
                                     AssistantMessage message = choice.getMessage();
                                     ChatGptRole role = message.getRole();
                                     String content = message.getContent();
                                     getUnitTestLogger().info("Response from " + role + ": " + content);
                                     List<OpenAIChatGptResponseToolCall> toolCalls = message.getToolCalls();
                                     assert toolCalls != null;
                                     OpenAIChatGptResponseToolCall toolCall = toolCalls.get(0);
                                     OpenAIChatGptResponseFunctionCall function = toolCall.getFunction();
                                     String name = function.getName();
                                     String arguments = function.getArguments();
                                     getUnitTestLogger().info("Function " + name + "(" + arguments + ")");
                                     return Future.succeededFuture();
                                 })
        );
    }

    @Test
    @Override
    public void testStreamWithToolCall() {
        this.async(() -> getKit().chatStream(
                getServiceMeta(),
                generateRequest(),
                chunk -> {
                    List<OpenAIChatGptResponseChunkChoice> choices = chunk.getChoices();
                    if (!choices.isEmpty()) {
                        OpenAIChatGptResponseChunkChoiceDelta delta = choices.get(0).getDelta();
                        ChatGptRole role = delta.getRole();
                        String contentAsText = delta.getContentAsText();
                        getUnitTestLogger().info("Response from " + role + ": " + contentAsText);
                        List<OpenAIChatGptResponseToolCall> toolCalls = delta.getToolCalls();
                        if (toolCalls != null) {
                            toolCalls.forEach(toolCall -> {
                                String name = toolCall.getFunction().getName();
                                String arguments = toolCall.getFunction().getArguments();
                                getUnitTestLogger().info("Function " + name + "(" + arguments + ")");
                            });
                        }
                    }
                },
                0,
                generateRequestId()
        ));
    }

    @Test
    @Override
    public void testStreamBufferWithToolCall() {
        this.async(() -> getKit().chatStream(
                                         getServiceMeta(),
                                         generateRequest(),
                                         0,
                                         generateRequestId()
                                 )
                                 .compose(resp -> {
                                     AssistantMessage message = resp.getMessage();
                                     ChatGptRole role = message.getRole();
                                     String content = message.getContent();
                                     getUnitTestLogger().info("Response from " + role + ": " + content);
                                     List<OpenAIChatGptResponseToolCall> toolCalls = message.getToolCalls();
                                     Assert.assertNotNull(toolCalls);
                                     Assert.assertFalse(toolCalls.isEmpty());
                                     for (OpenAIChatGptResponseToolCall toolCall : toolCalls) {
                                         String name = toolCall.getFunction().getName();
                                         String arguments = toolCall.getFunction().getArguments();
                                         getUnitTestLogger().info("Function " + name + "(" + arguments + ")");
                                     }
                                     return Future.succeededFuture();
                                 })
        );
    }

    @Override
    protected String getServiceName() {
        return "gpt-4-o";
    }
}
