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
public class AzureChatTest3 extends AzureChatTestCore {
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

    @TestUnit(skip = false)
    public Future<Void> test2() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chat(getServiceMeta(), parameters.toJsonObject(), requestId)
                .compose(resp -> {
                    getLogger().info("RESP", resp);
                    return Future.succeededFuture();
                });

        /*
        ```json
        {
            "choices":[
                {
                    "content_filter_results":{},
                    "finish_reason":"tool_calls",
                    "index":0,
                    "logprobs":null,
                    "message":{
                        "content":null,
                        "role":"assistant",
                        "tool_calls":[
                            {
                                "function":{
                                    "arguments":"{\"keywords\":[\"天猫平台\",\"商品销售额\",\"每年\"]}",
                                    "name":"searchDataSet"
                                },
                                "id":"call_9E5rm9w7tIPGoi0XHp6mESBM",
                                "type":"function"
                            }
                        ]
                    }
                }
            ],
            "created":1724132680,
            "id":"chatcmpl-9yBxYIDGdu6cxR2XqpV4w1gj1gkp5",
            "model":"gpt-4o-2024-05-13",
            "object":"chat.completion",
            "prompt_filter_results":[
                {
                    "prompt_index":0,
                    "content_filter_results":{
                        "hate":{"filtered":false,"severity":"safe"},
                        "self_harm":{"filtered":false,"severity":"safe"},
                        "sexual":{"filtered":false,"severity":"safe"},
                        "violence":{"filtered":false,"severity":"safe"}
                    }
                }
            ],
            "system_fingerprint":"fp_80a1bad4c7",
            "usage":{"completion_tokens":25,"prompt_tokens":105,"total_tokens":130}
        }
        ```
         */
    }

    @TestUnit(skip = false)
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
