package io.github.sinri.AiOnHttpMix.test.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.*;
import io.github.sinri.AiOnHttpMix.test.dashscope.DashscopeTestCore;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class QwenVLUnitTest extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenVLRequest chatRequest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
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
                        ));
    }

    @Test
    public void test1() {
        getLogger().info("REQ", chatRequest.toJsonObject());
        String requestId = UUID.randomUUID().toString();
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatVL(
                            getServiceMeta(),
                            chatRequest.toJsonObject(),
                            requestId
                    )
                    .compose(resp -> {
                        getLogger().info("resp", resp);
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void test2() {
        getLogger().info("REQ", chatRequest.toJsonObject());
        String requestId = UUID.randomUUID().toString();
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatVL(
                            getServiceMeta(),
                            chatRequest,
                            requestId
                    )
                    .compose(resp -> {
                        getLogger().info("resp");
                        List<QwenVLResponse.Choice> choices = resp.getOutput().getChoices();
                        QwenVLResponse.Choice choice = choices.get(0);
                        QwenVLOutputMessage message = choice.getMessage();
                        QwenVLRole role = message.getRole();
                        String finishReason = choice.getFinishReason();
                        getLogger().info("Role: " + role + " | Finish Reason: " + finishReason);
                        List<QwenVLMessageContentItem> content = message.getContent();
                        content.forEach(item -> {
                            getLogger().info("Content Item", new JsonObject()
                                    .put("text", item.getText())
                                    .put("image", item.getImage())
                            );
                        });

                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void test3() {
        getLogger().info("REQ", chatRequest.toJsonObject());
        String requestId = UUID.randomUUID().toString();
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatVLStreamWithStringHandler(
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
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void test4() {
        getLogger().info("REQ", chatRequest.toJsonObject());
        String requestId = UUID.randomUUID().toString();
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatVLStreamWithChunkHandler(
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
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void test5() {
        getLogger().info("REQ", chatRequest.toJsonObject());
        String requestId = UUID.randomUUID().toString();
        KeelAsyncKit.pseudoAwait(promise -> {
            qwenKit.chatVLStreamWithBuffer(
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
                    })
                    .onComplete(promise);
        });
    }
}
