package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoiceDelta;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Test for Azure with SSE query.
 */
public class AzureChatTest2 extends AzureChatTestCore {
    private OpenAIChatGptRequest parameters;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    parameters = OpenAIChatGptRequest.create()
                            .addMessage(m -> m.system("你是一个专业的IT工程师。"))
                            .addMessage(m -> m.user("Java 17和Java 21的差别是什么？"));
                    return Future.succeededFuture();
                });
    }

    @TestUnit(skip = false)
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chatStream(
                        getServiceMeta(),
                        parameters.toJsonObject(),
                        chunkString -> {
                            getLogger().info("ChunkString | " + chunkString);
                        },
                        requestId
                );
    }

    @TestUnit(skip = false)
    public Future<Void> test2() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
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
                });
    }
}
