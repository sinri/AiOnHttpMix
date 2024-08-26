package io.github.sinri.AiOnHttpMix.test.volces;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.AiOnHttpMix.volces.v3.mixin.chunk.VolcesChatCompletionsResponseChunkMixin;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class VolcesTest3 extends VolcesTestCore {
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
                                    .setContent("你是一个专业的IT工程师。")
                            )
                            .addMessage(VolcesKit.MessageParam.create()
                                    .setRole(VolcesKit.ChatRole.user)
                                    .setContent("Java 17和Java 21的差别是什么？")
                            )
                            .setStream(true);
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
                    getLogger().info("role: "+message.getRole());
                    getLogger().info("content: "+message.getContent());
                    return Future.succeededFuture();
                });
    }
}
