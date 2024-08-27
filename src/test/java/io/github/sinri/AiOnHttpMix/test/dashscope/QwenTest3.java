package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class QwenTest3 extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenRequest chatRequest;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    qwenKit = new QwenKit();
                    chatRequest = QwenRequest.create()
                            .setModel(QwenKit.QwenModel.QWEN_PLUS)
                            .handleInput(input -> input
                                    .addSystemMessage("你是个IT专家")
                                    .addUserMessage("IPv4的内网网段划分策略")
                            )
                            .handleParameters(p -> p
                                    .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
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
                    QwenResponseChunk.OutputChunkForMessageResponse output = chatMessageResponseInChunk.getOutput();
                    List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
                    QwenResponseChunk.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                    QwenMessage message = choice.getMessage();
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
                    QwenResponseInMessageFormat.OutputForMessageResponse output = chatMessageResponse.getOutput();
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = output.getChoices();
                    if (choices != null && !choices.isEmpty()) {
                        QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                        QwenMessage message = choice.getMessage();
                        QwenRole role = message.getRole();
                        String content = message.getContent();
                        getLogger().info(role + " | " + content + " | " + choice.getFinishReason());
                    }
                    return Future.succeededFuture();
                });
    }
}
