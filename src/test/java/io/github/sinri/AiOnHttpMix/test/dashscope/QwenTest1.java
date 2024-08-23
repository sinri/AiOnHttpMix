package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatMessageResponseMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenChatRequestMixin;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt.QwenMessageMixin;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;


import java.util.List;
import java.util.UUID;

public class QwenTest1 extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenKit.ChatRequest chatRequest;
    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v->{
                    qwenKit = new QwenKit();
                    chatRequest = QwenKit.ChatRequest.create()
                            .setModel(QwenKit.QwenModel.QWEN_PLUS)
                            .handleInput(input -> input
                                    .addSystemMessage("你是个IT专家")
                                    .addUserMessage("IPv4的内网网段划分策略")
                            )
                            .handleParameters(p -> p
                                    .setResultFormat(QwenChatRequestMixin.Parameters.ResultFormat.message)
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

    @TestUnit
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
                        getLogger().info(role + " | " + content + " | " + choice.getFinishReason());
                    }
                    return Future.succeededFuture();
                });
    }
}
