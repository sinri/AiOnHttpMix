package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoiceDelta;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Test for Azure with SSE query.
 */
public class AzureChatTest4 extends AzureChatTestCore {
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
    public Future<Void> test3() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chatStream(
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
        getLogger().info("REQ", parameters.toJsonObject());

        AtomicReference<String> currentToolCallIdRef = new AtomicReference<>();
        AtomicReference<String> currentToolCallTypeRef = new AtomicReference<>();

        return new ChatGPTKit()
                .chatStream(
                        getServiceMeta(),
                        parameters,
                        chunk -> {
                            try {
                                getLogger().info("Chunk:", chunk.cloneAsJsonObject());
                                List<OpenAIChatGptResponseChunkChoice> choices = chunk.getChoices();
                                if (choices.isEmpty()) return;
                                OpenAIChatGptResponseChunkChoice choiceInChunk = choices.get(0);
                                OpenAIChatGptResponseChunkChoiceDelta delta = choiceInChunk.getDelta();
                                if (delta == null) return;
                                ChatGptRole role = delta.getRole();
                                if (role != null) {
                                    getLogger().info("Role: " + role);
                                }
                                String contentAsText = delta.getContentAsText();
                                if (contentAsText != null) {
                                    getLogger().info("Content as Text: " + contentAsText);
                                }
                                List<OpenAIChatGptResponseToolCall> toolCalls = delta.getToolCalls();
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
                                            OpenAIChatGptResponseFunctionCall functionCall = toolCall.getFunction();
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
        getLogger().info("REQ", parameters.toJsonObject());

        return new ChatGPTKit()
                .chatStream(
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
