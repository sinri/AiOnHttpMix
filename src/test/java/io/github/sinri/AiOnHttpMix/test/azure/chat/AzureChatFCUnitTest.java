package io.github.sinri.AiOnHttpMix.test.azure.chat;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk.OpenAIChatGptResponseChunkChoiceDelta;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.request.OpenAIChatGptRequest;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class AzureChatFCUnitTest extends AzureChatUnitTestCore {
    private OpenAIChatGptRequest parameters;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        parameters = OpenAIChatGptRequest.create()
                .addTool(t -> t.functionName("searchDataSet")
                        .functionDescription("根据信息查询可能的数据集")
                        .propertyAsString("keywords", "由一组关键字字符串组成的JSON数组")
                )
                .addMessage(m -> m.system("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。"))
                .addMessage(m -> m.user("每年在天猫平台上达成的商品销售额"));
    }

    @Test
    public void testSyncRaw() throws Exception {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        KeelAsyncKit.pseudoAwait(promise -> {
            new ChatGPTKit().chat(
                            getServiceMeta(),
                            parameters.toJsonObject(),
                            requestId
                    )
                    .compose(resp -> {
                        getLogger().info("RESP", resp);
                        Assert.assertNotNull(resp);
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });

    }

    @Test
    public void testStreamRaw() throws Exception {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        KeelAsyncKit.pseudoAwait(promise -> {
            new ChatGPTKit().chatStream(
                            getServiceMeta(),
                            parameters.toJsonObject(),
                            s -> {
                                getLogger().info("ChunkString | " + s);
                                Assert.assertFalse(s.isEmpty());
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

        KeelAsyncKit.pseudoAwait(promise -> {
            new ChatGPTKit()
                    .chat(getServiceMeta(), parameters, requestId)
                    .compose(resp -> {
                        List<OpenAIChatGptResponseChoice> choices = resp.getChoices();
                        Assert.assertFalse(choices.isEmpty());
                        OpenAIChatGptResponseChoice choice = choices.get(0);
                        AssistantMessage message = choice.getMessage();
                        ChatGptRole role = message.getRole();
                        String content = message.getContent();
                        var toolCalls = message.getToolCalls();

                        getLogger().info("RESP FROM " + role);
                        if (content != null) {
                            getLogger().info("Content: " + content);
                        }
                        Assert.assertNotNull(toolCalls);
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

                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testStream() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());

        AtomicReference<String> currentToolCallIdRef = new AtomicReference<>();
        AtomicReference<String> currentToolCallTypeRef = new AtomicReference<>();

        KeelAsyncKit.pseudoAwait(promise -> {
            new ChatGPTKit().chatStream(
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
                                    Assert.assertNotNull(toolCalls);
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
                                } catch (Throwable e) {
                                    getLogger().exception(e, "IN CHUNK HANDLER");
                                    Assert.fail(e.getMessage());
                                }
                            },
                            requestId)
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testStreamBuffer() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", parameters.toJsonObject());
        KeelAsyncKit.pseudoAwait(promise -> {
            new ChatGPTKit().chatStream(
                            getServiceMeta(),
                            parameters,
                            requestId
                    )
                    .compose(choice -> {
                        Assert.assertNotNull(choice);
                        getLogger().info("choice", choice.cloneAsJsonObject());
                        Assert.assertNotNull(choice.getMessage());
                        List<OpenAIChatGptResponseToolCall> toolCalls = choice.getMessage().getToolCalls();
                        Assert.assertNotNull(toolCalls);
                        for (OpenAIChatGptResponseToolCall toolCall : toolCalls) {
                            OpenAIChatGptResponseFunctionCall function = toolCall.getFunction();
                            Assert.assertNotNull(function);
                            Assert.assertNotNull(function.getName());
                            Assert.assertNotNull(function.getArguments());
                        }

                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
