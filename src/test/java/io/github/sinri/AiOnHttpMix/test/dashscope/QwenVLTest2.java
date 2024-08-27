package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.*;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class QwenVLTest2 extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenVLRequest chatRequest;

    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    qwenKit = new QwenKit();
                    chatRequest = QwenVLRequest.create()
                            .setModel(QwenKit.QwenVLModel.QWEN_VL_PLUS)
                            .setInput(QwenVLRequest.Input.create()
                                    .addMessage(QwenVLInputMessage.create()
                                            .setRole(QwenVLRole.user)
                                            .addContentItem(QwenVLMessageContentItem.create()
                                                    .setImage("https://pics6.baidu.com/feed/8601a18b87d6277f6438cea9e7199b3ee924fc2e.jpeg@f_auto?token=cb586c2ff83aefa74a3365453f55eaba")
                                            )
                                            .addContentItem(QwenVLMessageContentItem.create()
                                                    .setText("图片中的人是谁")
                                            )
                                    ))
                            .setParameters(QwenVLRequest.Parameters.create()
                                    .setIncrementalOutput(true)
                            );
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test1() {
        getLogger().info("REQ", chatRequest.toJsonObject());
        String requestId = UUID.randomUUID().toString();
        return qwenKit.chatVLStreamWithStringHandler(
                        getServiceMeta(),
                        chatRequest.toJsonObject(),
                        s -> {
                            getLogger().info("S | " + s);
                        },
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("fin");
                    return Future.succeededFuture();
                });
    }

    @TestUnit(skip = false)
    public Future<Void> test2() {
        getLogger().info("REQ", chatRequest.toJsonObject());
        String requestId = UUID.randomUUID().toString();
        return qwenKit.chatVLStreamWithChunkHandler(
                        getServiceMeta(),
                        chatRequest,
                        chatMessageResponseInChunk -> {
                            QwenVLResponse.Output output = chatMessageResponseInChunk.getOutput();
                            List<QwenVLResponse.Choice> choices = output.getChoices();
                            QwenVLResponse.Choice choice = choices.get(0);
                            String finishReason = choice.getFinishReason();
                            QwenVLOutputMessage message = choice.getMessage();
                            QwenVLRole role = message.getRole();
                            List<QwenVLMessageContentItem> content = message.getContent();
                            getLogger().info("Role: " + role + " | Finish reason: " + finishReason);
                            content.forEach(item -> {
                                getLogger().info("content item", new JsonObject()
                                        .put("text", item.getText())
                                        .put("image", item.getImage())
                                );
                            });
                        },
                        requestId
                )
                .compose(resp -> {
                    getLogger().info("fin");
                    return Future.succeededFuture();
                });
    }

    @TestUnit(skip = false)
    public Future<Void> test3() {
        getLogger().info("REQ", chatRequest.toJsonObject());
        String requestId = UUID.randomUUID().toString();
        return qwenKit.chatVLStreamWithBuffer(
                        getServiceMeta(),
                        chatRequest,
                        requestId
                )
                .compose(chatMessageResponseInChunk -> {
                    QwenVLResponse.Output output = chatMessageResponseInChunk.getOutput();
                    List<QwenVLResponse.Choice> choices = output.getChoices();
                    QwenVLResponse.Choice choice = choices.get(0);
                    String finishReason = choice.getFinishReason();
                    QwenVLOutputMessage message = choice.getMessage();
                    QwenVLRole role = message.getRole();
                    List<QwenVLMessageContentItem> content = message.getContent();
                    getLogger().info("Role: " + role + " | Finish reason: " + finishReason);
                    content.forEach(item -> {
                        getLogger().info("content item", new JsonObject()
                                .put("text", item.getText())
                                .put("image", item.getImage())
                        );
                    });
                    return Future.succeededFuture();
                });
    }
}
