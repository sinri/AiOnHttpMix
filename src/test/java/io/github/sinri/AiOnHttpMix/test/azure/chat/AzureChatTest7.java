package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Test for Azure with Non SSE query.
 */
public class AzureChatTest7 extends AzureChatTestCore {
    private OpenAIChatGptRequest parameters;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    parameters = OpenAIChatGptRequest.create()
                            .addTool(t -> t.functionName("searchDataSet")
                                    .functionDescription("根据信息查询可能的数据集")
                                    .propertyAsString("keywords", "由一组关键字字符串组成的JSON数组")
                            )
                            .addMessage(m -> m.system("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。"))
                            .addMessage(m -> m.user("每年在天猫平台上达成的商品销售额"));
                    return Future.succeededFuture();
                });
    }


    @TestUnit
    public Future<Void> test4() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chat(getServiceMeta(), parameters, requestId)
                .compose(resp -> {
                    OpenAIChatGptResponseChoice choice = resp.getChoices().get(0);
                    AssistantMessage message = choice.getMessage();
                    ChatGptRole role = message.getRole();
                    String content = message.getContent();
                    var toolCalls = message.getToolCalls();

                    getLogger().info("RESP FROM " + role);
                    if (content != null) {
                        getLogger().info("Content: " + content);
                    }
                    if (toolCalls != null) {
                        toolCalls.forEach(toolCall -> {
                            String type = toolCall.getType();
                            getLogger().info("ToolCall, ID: " + toolCall.getId() + " Type: " + type);
                            if (Objects.equals("function", type)) {
                                getLogger().info(
                                        "As Function Tool Call",
                                        new JsonObject()
                                                .put("function_name", toolCall.getFunction().getName())
                                                .put("arguments", toolCall.getFunction().getArguments())
                                );
                            }
                        });
                    }

                    return Future.succeededFuture();
                });
    }
}
