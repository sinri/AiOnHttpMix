package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoiceDelta;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class AzureChatPureUnitTest extends AzureChatUnitTestCore {
    private OpenAIChatGptRequest parameters;

    @Before
    public void setup() throws Exception {
        super.setUp();

        parameters = OpenAIChatGptRequest.create()
                .addMessage(m -> m.system("你是一个专业的IT工程师。"))
                .addMessage(m -> m.user("Java 17和Java 21的差别是什么？"));
    }

    @Test
    public void testRaw() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        Keel.pseudoAwait(promise -> {
            new ChatGPTKit()
                    .chat(getServiceMeta(), parameters.toJsonObject(), requestId)
                    .compose(resp -> {
                        getLogger().info("RESP", resp);
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testRawStream() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        Keel.pseudoAwait(promise -> {
            new ChatGPTKit().chatStream(
                            getServiceMeta(),
                            parameters.toJsonObject(),
                            chunkString -> {
                                getLogger().info("ChunkString | " + chunkString);
                                Assert.assertTrue(chunkString.length() > 0);
                            },
                            requestId
                    )
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testSync() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());
        Keel.pseudoAwait(promise -> {
            new ChatGPTKit()
                    .chat(getServiceMeta(), parameters, requestId)
                    .compose(resp -> {
                        List<OpenAIChatGptResponseChoice> choices = resp.getChoices();
                        Assert.assertFalse(choices.isEmpty());
                        OpenAIChatGptResponseChoice choice = choices.get(0);
                        Assert.assertNotNull(choice);
                        var message = choice.getMessage();
                        Assert.assertNotNull(message);
                        ChatGptRole role = message.getRole();
                        Assert.assertNotNull(role);
                        String content = message.getContent();
                        Assert.assertNotNull(content);
                        Assert.assertFalse(content.isEmpty());
                        getLogger().info("RESP | " + role + " | " + content);
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testStream() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        Keel.pseudoAwait(promise -> {
            new ChatGPTKit()
                    .chatStream(
                            getServiceMeta(),
                            parameters,
                            chunk -> {
                                List<OpenAIChatGptResponseChunkChoice> choices = chunk.getChoices();
                                if (choices.isEmpty()) return;
                                OpenAIChatGptResponseChunkChoice choiceInChunk = choices.get(0);
                                OpenAIChatGptResponseChunkChoiceDelta delta = choiceInChunk.getDelta();
                                if (delta == null) return;
                                String contentAsText = delta.getContentAsText();
                                if (contentAsText == null) return;
                                System.out.print(contentAsText);
                            },
                            requestId
                    )
                    .eventually(() -> {
                        System.out.println();
                        return Future.succeededFuture();
                    })
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
