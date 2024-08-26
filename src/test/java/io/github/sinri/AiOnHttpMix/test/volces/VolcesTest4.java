package io.github.sinri.AiOnHttpMix.test.volces;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.AiOnHttpMix.volces.v3.mixin.chunk.VolcesChatCompletionsResponseChunkMixin;
import io.github.sinri.AiOnHttpMix.volces.v3.mixin.response.VolcesChatResponseMessageToolCallMixin;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class VolcesTest4 extends VolcesTestCore {
    private VolcesKit volcesKit;
    private VolcesKit.ChatCompletionsRequest chatCompletionsRequest;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    volcesKit = new VolcesKit();
                    chatCompletionsRequest = VolcesKit.ChatCompletionsRequest.create()
                            .addMessage(VolcesKit.MessageParam.create()
                                    .setRole(VolcesKit.ChatRole.system)
                                    .setContent("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。")
                            )
                            .addMessage(VolcesKit.MessageParam.create()
                                    .setRole(VolcesKit.ChatRole.user)
                                    .setContent("每年在天猫平台上达成的商品销售额")
                            )
                            .addTool(VolcesKit.ToolParam.create(
                                    VolcesKit.FunctionDefinition.builder()
                                            .functionName("searchDataSet")
                                            .functionDescription("根据信息查询可能的数据集")
                                            .propertyAsString("keywords", "由一组关键字字符串组成的JSON数组")
                                            .build()
                            ))
                            .setStream(true);

                    getLogger().info("REQ", chatCompletionsRequest.toJsonObject());

                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        return volcesKit.chatStreamWithStringHandler(
                        getServiceMeta(),
                        chatCompletionsRequest.toJsonObject(),
                        s -> {
                            getLogger().info("chunk | " + s);
                        },
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("fin");
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test2() {
        String requestId = UUID.randomUUID().toString();
        return volcesKit.chatStreamWithChunkHandler(
                        getServiceMeta(),
                        chatCompletionsRequest,
                        chunk -> {
                            List<VolcesChatCompletionsResponseChunkMixin.StreamChoice> choices = chunk.getChoices();
                            VolcesChatCompletionsResponseChunkMixin.StreamChoice choice = choices.get(0);
                            VolcesChatCompletionsResponseChunkMixin.ChoiceDelta delta = choice.getDelta();
                            VolcesKit.ChatRole role = delta.getRole();
                            if (role != null) {
                                getLogger().info("role: " + role);
                            }
                            String content = delta.getContent();
                            if (content != null) {
                                getLogger().info("content: " + content);
                            }
                            List<VolcesChatCompletionsResponseChunkMixin.ChoiceDeltaToolCall> toolCalls = delta.getToolCalls();
                            if (toolCalls != null) {
                                toolCalls.forEach(toolCall -> {
                                    String type = toolCall.getType();
                                    getLogger().info("toolCall: " + toolCall.getId() + " | " + type);
                                    VolcesChatCompletionsResponseChunkMixin.FunctionCallChunk function = toolCall.getFunction();
                                    if (function != null) {
                                        getLogger().info("function: " + function.getName() + " | " + function.getArguments());
                                    }
                                });
                            }
                        },
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("fin");
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test3() {
        String requestId = UUID.randomUUID().toString();
        return volcesKit.chatStreamWithBuffer(
                        getServiceMeta(),
                        chatCompletionsRequest,
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("resp");
                    List<VolcesKit.ChatCompletionsResponse.Choice> choices = resp.getChoices();
                    VolcesKit.ChatCompletionsResponse.Choice choice = choices.get(0);
                    var message = choice.getMessage();
                    getLogger().info("role: " + message.getRole());
                    String content = message.getContent();
                    if (content != null) {
                        getLogger().info("content: " + content);
                    }
                    var toolCalls = message.getToolCalls();
                    if (toolCalls != null) {
                        toolCalls.forEach(toolCall -> {
                            getLogger().info("toolCall: " + toolCall.getType() + " id: " + toolCall.getId());
                            VolcesKit.FunctionParam function = toolCall.getFunction();
                            if (function != null) {
                                getLogger().info("function: " + function.getName() + " | " + function.getArguments());
                            }
                        });
                    }
                    return Future.succeededFuture();
                });
    }
}
