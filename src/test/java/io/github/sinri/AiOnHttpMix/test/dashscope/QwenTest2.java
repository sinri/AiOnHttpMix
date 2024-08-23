package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatMessageResponseMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatRequestMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenMessageMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenToolCallMixin;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class QwenTest2 extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenKit.ChatRequest chatRequest;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    qwenKit = new QwenKit();
                    chatRequest = QwenKit.ChatRequest.create()
                            .setModel(QwenKit.QwenModel.QWEN_PLUS)
                            .handleInput(input -> input
                                    .addSystemMessage("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。")
                                    .addUserMessage("每年在天猫平台上达成的商品销售额")
                            )
                            .handleParameters(p -> p
                                    .setResultFormat(QwenChatRequestMixin.Parameters.ResultFormat.message)
                                    .addTool(QwenKit.ToolDefinition.asFunction(
                                            "searchDataSet",
                                            "根据信息查询可能的数据集",
                                            List.of(
                                                    new QwenKit.ToolDefinition.FunctionArgument(
                                                            "keywords",
                                                            "String",
                                                            "由一组关键字字符串组成的JSON数组",
                                                            true
                                                    )
                                            )
                                    ))
                            );
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        return qwenKit.chat(
                        getServiceMeta(),
                        chatRequest.toJsonObject(),
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("resp", resp);
                    return Future.succeededFuture();
                });
    }

    @TestUnit(skip = false)
    public Future<Void> test2() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());

        return qwenKit.chatForMessageResponse(
                        getServiceMeta(),
                        chatRequest,
                        requestId
                )
                .compose(chatMessageResponse -> {
                    getLogger().info("resp comes");
                    QwenChatMessageResponseMixin.OutputForMessageResponse output = chatMessageResponse.getOutput();
                    List<QwenChatMessageResponseMixin.OutputForMessageResponse.Choice> choices = output.getChoices();
                    if (choices != null && !choices.isEmpty()) {
                        QwenChatMessageResponseMixin.OutputForMessageResponse.Choice choice = choices.get(0);
                        QwenKit.Message message = choice.getMessage();
                        QwenMessageMixin.Role role = message.getRole();
                        String content = message.getContent();
                        List<QwenKit.ToolCall> toolCalls = message.getToolCalls();
                        getLogger().info(role + " | " + content + " | " + choice.getFinishReason());
                        if (toolCalls != null && !toolCalls.isEmpty()) {
                            toolCalls.forEach(toolCall -> {
                                String toolCallId = toolCall.getId();
                                getLogger().info("ToolCallId: " + toolCallId + " | type: " + toolCall.getType());
                                QwenToolCallMixin.FunctionCall function = toolCall.getFunction();
                                getLogger().info("Function: " + function.getName() + " | " + function.getArguments());
                            });
                        }

                    }
                    return Future.succeededFuture();
                });
    }
}
