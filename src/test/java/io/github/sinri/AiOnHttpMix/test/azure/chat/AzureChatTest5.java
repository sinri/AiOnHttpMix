package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Test for Azure with Non SSE query.
 */
public class AzureChatTest5 extends AzureChatTestCore {
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


    @TestUnit
    public Future<Void> test() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chat(getServiceMeta(), parameters, requestId)
                .compose(resp -> {
                    OpenAIChatGptResponseChoice choice = resp.getChoices().get(0);
                    var message = choice.getMessage();
                    ChatGptRole role = message.getRole();
                    String content = message.getContent();
                    getLogger().info("RESP | " + role + " | " + content);
                    return Future.succeededFuture();
                });
    }


}
