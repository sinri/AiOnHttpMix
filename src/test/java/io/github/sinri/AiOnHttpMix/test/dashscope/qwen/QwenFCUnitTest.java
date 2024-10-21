package io.github.sinri.AiOnHttpMix.test.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolCall;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolDefinition;
import io.github.sinri.AiOnHttpMix.test.dashscope.DashscopeTestCore;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class QwenFCUnitTest extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenRequest chatRequest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        qwenKit = new QwenKit();
        chatRequest = QwenRequest.create()
                .setModel(QwenKit.QwenModel.QWEN_PLUS)
                .handleInput(input -> input
                        .addSystemMessage("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。")
                        .addUserMessage("每年在天猫平台上达成的商品销售额")
                )
                .handleParameters(p -> p
                        .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
                        .addTool(QwenToolDefinition.asFunction(
                                "searchDataSet",
                                "根据信息查询可能的数据集",
                                List.of(
                                        new QwenToolDefinition.FunctionArgument(
                                                "keywords",
                                                "String",
                                                "由一组关键字字符串组成的JSON数组",
                                                true
                                        )
                                )
                        ))
                );
    }

    @Test
    public void test1() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chat(
                            getServiceMeta(),
                            chatRequest.toJsonObject(),
                            requestId
                    )
                    .compose(resp -> {
                        getLogger().info("resp", resp);
                        Assert.assertNotNull(resp);
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });

    }

    @Test
    public void test2() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());

        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatForMessageResponse(
                            getServiceMeta(),
                            chatRequest,
                            requestId
                    )
                    .compose(chatMessageResponse -> {
                        getLogger().info("resp comes");
                        QwenResponseInMessageFormat.OutputForMessageResponse output = chatMessageResponse.getOutput();
                        Assert.assertNotNull(output);
                        List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = output.getChoices();
                        Assert.assertNotNull(choices);
                        Assert.assertFalse(choices.isEmpty());
                        QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                        QwenMessage message = choice.getMessage();
                        QwenRole role = message.getRole();
                        String content = message.getContent();
                        List<QwenToolCall> toolCalls = message.getToolCalls();
                        getLogger().info(role + " | " + content + " | " + choice.getFinishReason());
                        Assert.assertNotNull(toolCalls);
                        Assert.assertFalse(toolCalls.isEmpty());
                        toolCalls.forEach(toolCall -> {
                            String toolCallId = toolCall.getId();
                            getLogger().info("ToolCallId: " + toolCallId + " | type: " + toolCall.getType());
                            QwenToolCall.FunctionCall function = toolCall.getFunction();
                            getLogger().info("Function: " + function.getName() + " | " + function.getArguments());
                        });

                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void test3() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatStreamWithStringHandler(
                            getServiceMeta(),
                            chatRequest.toJsonObject(),
                            s -> {
                                getLogger().info("CHUCK | " + s);
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
    public void test4() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatStreamWithChunkHandler(
                            getServiceMeta(),
                            chatRequest,
                            chatMessageResponseInChunk -> {
                                QwenResponseChunk.OutputChunkForMessageResponse output = chatMessageResponseInChunk.getOutput();
                                List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
                                QwenResponseChunk.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                                QwenMessage message = choice.getMessage();
                                QwenRole role = message.getRole();
                                getLogger().info("ROLE: " + role);
                                String content = message.getContent();
                                if (content != null) {
                                    getLogger().info("CONTENT: " + content);
                                }
                                List<QwenToolCall> toolCalls = message.getToolCalls();
                                if (toolCalls != null) {
                                    toolCalls.forEach(toolCall -> {
                                        String type = toolCall.getType();
                                        getLogger().info("TYPE: " + type + " ID: " + toolCall.getId() + " INDEX: " + toolCall.getIndex());
                                        QwenToolCall.FunctionCall function = toolCall.getFunction();
                                        getLogger().info("FUNCTION: " + function.getName() + " | " + function.getArguments());
                                    });
                                }
                                getLogger().info("Finish Reason: " + choice.getFinishReason());
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
    public void test5() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatStreamWithBuffer(
                            getServiceMeta(),
                            chatRequest,
                            requestId
                    )
                    .compose(chatMessageResponse -> {
                        getLogger().info("resp comes");
                        QwenResponseInMessageFormat.OutputForMessageResponse output = chatMessageResponse.getOutput();
                        List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = output.getChoices();
                        Assert.assertNotNull(choices);
                        Assert.assertFalse(choices.isEmpty());
                        QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                        Assert.assertNotNull(choice);
                        getLogger().info("choice", choice.cloneAsJsonObject());
                        QwenMessage message = choice.getMessage();
                        getLogger().info("message", message.toJsonObject());
                        Assert.assertNotNull(message);
                        QwenRole role = message.getRole();
                        Assert.assertNotNull(role);
                        String content = message.getContent();
                        getLogger().info(role + " | " + content + " | " + choice.getFinishReason());
                        List<QwenToolCall> toolCalls = message.getToolCalls();
                        Assert.assertNotNull(toolCalls);
                        Assert.assertFalse(toolCalls.isEmpty());
                        toolCalls.forEach(toolCall -> {
                            String toolCallId = toolCall.getId();
                            getLogger().info("ToolCallId: " + toolCallId + " | type: " + toolCall.getType());
                            QwenToolCall.FunctionCall function = toolCall.getFunction();
                            getLogger().info("Function: " + function.getName() + " | " + function.getArguments());
                        });
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
