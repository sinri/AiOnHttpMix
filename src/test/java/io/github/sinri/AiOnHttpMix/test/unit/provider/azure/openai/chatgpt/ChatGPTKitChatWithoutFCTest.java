package io.github.sinri.AiOnHttpMix.test.unit.provider.azure.openai.chatgpt;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoiceDelta;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForNonFC;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.List;

public class ChatGPTKitChatWithoutFCTest extends AbstractChatGPTKitChatUnitTest
        implements LLMUnitTestCoverageForNonFC<OpenAIChatGptRequest> {

    @Override
    public OpenAIChatGptRequest generateRequest() {
        return OpenAIChatGptRequest.create()
                                   .addMessage(msg -> msg.system("尔是朕的忠臣"))
                                   .addMessage(msg -> msg.user("近来边境之藩镇有谋反之心，又恐打草惊蛇，这可如何是好"));
    }

    @Test
    @TestPassed(time = "2025-02-13")
    public void testChatWithRaw() {
        async(() -> getKit().chat(
                                    getServiceMeta(),
                                    generateRequest().toJsonObject(),
                                    generateRequestId()
                            )
                            .compose(resp -> {
                                getUnitTestLogger().info("response", resp);
                                return Future.succeededFuture();
                            }));
    }


    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithoutToolCall() {
        async(() -> getKit().chat(
                                    getServiceMeta(),
                                    generateRequest(),
                                    generateRequestId()
                            )
                            .compose(resp -> {
                                AssistantMessage message = resp.getChoices().get(0).getMessage();
                                getUnitTestLogger().info("response, message from " + message.getRole() + ": " + message.getContent());
                                return Future.succeededFuture();
                            }));
    }


    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamWithoutToolCall() {
        async(() -> getKit().chatStream(
                getServiceMeta(),
                generateRequest(),
                chunk -> {
                    List<OpenAIChatGptResponseChunkChoice> choices = chunk.getChoices();
                    if (!choices.isEmpty()) {
                        OpenAIChatGptResponseChunkChoice chunkChoice = choices.get(0);
                        OpenAIChatGptResponseChunkChoiceDelta delta = chunkChoice.getDelta();
                        assert delta != null;
                        ChatGptRole role = delta.getRole();
                        String contentAsText = delta.getContentAsText();
                        getUnitTestLogger().info("Chunk from " + role + ": " + contentAsText);
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
        async(() -> getKit().chatStream(
                                    getServiceMeta(),
                                    generateRequest(),
                                    0,
                                    generateRequestId()
                            )
                            .compose(choice -> {
                                AssistantMessage message = choice.getMessage();
                                String content = message.getContent();
                                ChatGptRole role = message.getRole();
                                getUnitTestLogger().info("Chunk from " + role + ": " + content);
                                return Future.succeededFuture();
                            })
        );
    }

    @Override
    protected String getServiceName() {
        return "gpt-4-o";
    }
}