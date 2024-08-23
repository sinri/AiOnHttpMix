package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatMessageResponseInChunkMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatMessageResponseMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatRequestMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenMessageMixin;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class QwenTest3 extends DashscopeTestCore {
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
                                    .addSystemMessage("你是个IT专家")
                                    .addUserMessage("IPv4的内网网段划分策略")
                            )
                            .handleParameters(p -> p
                                    .setResultFormat(QwenChatRequestMixin.Parameters.ResultFormat.message)
                                    .setIncrementalOutput(true)
                            );
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test1() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        return qwenKit.chatStreamWithStringHandler(
                        getServiceMeta(),
                        chatRequest.toJsonObject(),
                        s -> {
                            getLogger().info("CHUNK | " + s);
                        },
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("stream over");
                    return Future.succeededFuture();
                });
    }

    @TestUnit(skip = false)
    public Future<Void> test2() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        return qwenKit.chatStreamWithChunkHandler(
                getServiceMeta(),
                chatRequest,
                chatMessageResponseInChunk -> {
                    getLogger().info("ChatMessageResponseInChunk");
                    QwenChatMessageResponseInChunkMixin.OutputChunkForMessageResponse output = chatMessageResponseInChunk.getOutput();
                    List<QwenChatMessageResponseInChunkMixin.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
                    QwenChatMessageResponseInChunkMixin.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                    QwenKit.Message message = choice.getMessage();
                    getLogger().info("ROLE: " + message.getRole() + " | " + message.getContent());
                    getLogger().info("Finish Reason: " + choice.getFinishReason());
                },
                requestId
        );
    }

    @TestUnit(skip = false)
    public Future<Void> test3() {
        String requestId = UUID.randomUUID().toString();
        getLogger().info("REQ", chatRequest.toJsonObject());
        return qwenKit.chatStreamWithBuffer(
                        getServiceMeta(),
                        chatRequest,
                        requestId
                )
                .compose(chatMessageResponse -> {
                    getLogger().info("resp buffered");
                    QwenChatMessageResponseMixin.OutputForMessageResponse output = chatMessageResponse.getOutput();
                    List<QwenChatMessageResponseMixin.OutputForMessageResponse.Choice> choices = output.getChoices();
                    if (choices != null && !choices.isEmpty()) {
                        QwenChatMessageResponseMixin.OutputForMessageResponse.Choice choice = choices.get(0);
                        QwenKit.Message message = choice.getMessage();
                        QwenMessageMixin.Role role = message.getRole();
                        String content = message.getContent();
                        getLogger().info(role + " | " + content + " | " + choice.getFinishReason());
                    }
                    return Future.succeededFuture();
                });
    }
}
