package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIMessageMixin;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIResponseChunkMixin;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIToolCallMixin;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Test for Azure with SSE query.
 */
public class AzureChatTest2 extends AzureChatTestCore {
    @TestUnit(skip = false)
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        ChatGPTKit.ChatCompletionsParameters parameters = ChatGPTKit.ChatCompletionsParameters.create();
        parameters
                .addMessage(m -> m.system("你是一个专业的IT工程师。"))
                .addMessage(m -> m.user("Java 17和Java 21的差别是什么？"));

        getLogger().info("REQ", parameters.toJsonObject());

        return ChatGPTKit.getInstance()
                .callChatCompletionsStream(
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
        ChatGPTKit.ChatCompletionsParameters parameters = ChatGPTKit.ChatCompletionsParameters.create();
        parameters
                .addMessage(m -> m.system("你是一个专业的IT工程师。"))
                .addMessage(m -> m.user("Java 17和Java 21的差别是什么？"));

        getLogger().info("REQ", parameters.toJsonObject());

        return ChatGPTKit.getInstance()
                .callChatCompletionsStream(
                        getServiceMeta(),
                        parameters,
                        chunk -> {
                            List<OpenAIResponseChunkMixin.ChoiceInChunk> choices = chunk.getChoices();
                            if (choices.isEmpty()) return;
                            OpenAIResponseChunkMixin.ChoiceInChunk choiceInChunk = choices.get(0);
                            OpenAIResponseChunkMixin.Delta delta = choiceInChunk.getDelta();
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

    @TestUnit(skip = false)
    public Future<Void> test3() {
        String requestId = UUID.randomUUID().toString();
        ChatGPTKit.ChatCompletionsParameters parameters = ChatGPTKit.ChatCompletionsParameters.create();
        parameters
                .addTool(t -> t.functionName("searchDataSet")
                        .functionDescription("根据信息查询可能的数据集")
                        .propertyAsString("keywords", "由一组关键字字符串组成的JSON数组")
                )
                .addMessage(m -> m.system("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。"))
                .addMessage(m -> m.user("每年在天猫平台上达成的商品销售额"));


        getLogger().info("REQ", parameters.toJsonObject());

        return ChatGPTKit.getInstance()
                .callChatCompletionsStream(
                        getServiceMeta(),
                        parameters.toJsonObject(),
                        s -> {
                            getLogger().info("ChunkString | " + s);
                        },
                        requestId);
    }

    @TestUnit(skip = false)
    public Future<Void> test4() {
        String requestId = UUID.randomUUID().toString();
        ChatGPTKit.ChatCompletionsParameters parameters = ChatGPTKit.ChatCompletionsParameters.create();
        parameters
                .addTool(t -> t.functionName("searchDataSet")
                        .functionDescription("根据信息查询可能的数据集")
                        .propertyAsString("keywords", "由一组关键字字符串组成的JSON数组")
                )
                .addMessage(m -> m.system("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。"))
                .addMessage(m -> m.user("每年在天猫平台上达成的商品销售额"));


        getLogger().info("REQ", parameters.toJsonObject());

        AtomicReference<String> currentToolCallIdRef = new AtomicReference<>();
        AtomicReference<String> currentToolCallTypeRef = new AtomicReference<>();

        return ChatGPTKit.getInstance()
                .callChatCompletionsStream(
                        getServiceMeta(),
                        parameters,
                        chunk -> {
                            try {
                                getLogger().info("Chunk:", chunk.cloneAsJsonObject());
                                List<OpenAIResponseChunkMixin.ChoiceInChunk> choices = chunk.getChoices();
                                if (choices.isEmpty()) return;
                                OpenAIResponseChunkMixin.ChoiceInChunk choiceInChunk = choices.get(0);
                                OpenAIResponseChunkMixin.Delta delta = choiceInChunk.getDelta();
                                if (delta == null) return;
                                OpenAIMessageMixin.ChatCompletionRequestMessageRole role = delta.getRole();
                                if (role != null) {
                                    getLogger().info("Role: " + role);
                                }
                                String contentAsText = delta.getContentAsText();
                                if (contentAsText != null) {
                                    getLogger().info("Content as Text: " + contentAsText);
                                }
                                List<ChatGPTKit.ToolCallInChunk> toolCalls = delta.getToolCalls();
                                if (toolCalls != null) {
                                    toolCalls.forEach(toolCall -> {
                                        getLogger().debug("TOOL CALL", toolCall.cloneAsJsonObject());

                                        String toolCallId = toolCall.getId();
                                        if (toolCallId != null) {
                                            getLogger().info("ToolCall ID: " + toolCallId);
                                            currentToolCallIdRef.set(toolCallId);
                                            currentToolCallTypeRef.set(null);
                                        }

                                        String toolCallType = toolCall.getType();
                                        if (toolCallType != null) {
                                            currentToolCallTypeRef.set(toolCallType);
                                        }
                                        getLogger().info("ToolCall Type: " + currentToolCallTypeRef.get());

                                        if (Objects.equals("function", currentToolCallTypeRef.get())) {
                                            OpenAIToolCallMixin.FunctionCall functionCall = toolCall.getFunction();
                                            String name = functionCall.getName();
                                            String arguments = functionCall.getArguments();
                                            getLogger().info("As Function", new JsonObject()
                                                    .put("name", name)
                                                    .put("arguments", arguments)
                                            );
                                        }
                                    });
                                }
                            } catch (Throwable e) {
                                getLogger().exception(e, "IN CHUNK HANDLER");
                            }
                        },
                        requestId);
    }

    @TestUnit(skip = false)
    public Future<Void> test5() {
        String requestId = UUID.randomUUID().toString();
        ChatGPTKit.ChatCompletionsParameters parameters = ChatGPTKit.ChatCompletionsParameters.create();
        parameters
                .addTool(t -> t.functionName("searchDataSet")
                        .functionDescription("根据信息查询可能的数据集")
                        .propertyAsString("keywords", "由一组关键字字符串组成的JSON数组")
                )
                .addMessage(m -> m.system("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。"))
                .addMessage(m -> m.user("每年在天猫平台上达成的商品销售额"));


        getLogger().info("REQ", parameters.toJsonObject());

        return ChatGPTKit.getInstance()
                .callChatCompletionsStream(
                        getServiceMeta(),
                        parameters,
                        requestId
                )
                .compose(assistantMessage -> {
                    getLogger().info("AssistantMessage", assistantMessage.toJsonObject());
                    return Future.succeededFuture();
                });
    }
}
