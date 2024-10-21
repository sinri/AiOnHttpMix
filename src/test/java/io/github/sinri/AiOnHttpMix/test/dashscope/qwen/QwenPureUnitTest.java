package io.github.sinri.AiOnHttpMix.test.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.test.dashscope.DashscopeTestCore;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class QwenPureUnitTest extends DashscopeTestCore {
    private QwenKit qwenKit;
    private QwenRequest chatRequest;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        qwenKit = new QwenKit();
        chatRequest = QwenRequest.create()
                .setModel(QwenKit.QwenModel.QWEN_PLUS)
                .handleInput(input -> input
                        .addSystemMessage("你是个IT专家")
                        .addUserMessage("IPv4的内网网段划分策略")
                )
                .handleParameters(p -> p
                        .setResultFormat(QwenRequest.Parameters.ResultFormat.message)
                );
    }

    @Test
    public void test1() throws Exception {
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
                        getLogger().info(role + " | " + content + " | " + choice.getFinishReason());
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
                                getLogger().info("CHUNK | " + s);
                            },
                            requestId
                    )
                    .compose(resp -> {
                        getLogger().info("stream over");
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
                                getLogger().info("ChatMessageResponseInChunk");
                                QwenResponseChunk.OutputChunkForMessageResponse output = chatMessageResponseInChunk.getOutput();
                                Assert.assertNotNull(output);
                                List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
                                Assert.assertNotNull(choices);
                                QwenResponseChunk.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                                QwenMessage message = choice.getMessage();
                                Assert.assertNotNull(message);
                                Assert.assertFalse(message.getContent().isEmpty());
                                getLogger().info("ROLE: " + message.getRole() + " | " + message.getContent());
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
                        getLogger().info("resp buffered");
                        QwenResponseInMessageFormat.OutputForMessageResponse output = chatMessageResponse.getOutput();
                        Assert.assertNotNull(output);
                        List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = output.getChoices();
                        Assert.assertNotNull(choices);
                        Assert.assertFalse(choices.isEmpty());
                        QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                        Assert.assertNotNull(choice);
                        QwenMessage message = choice.getMessage();
                        Assert.assertNotNull(message);
                        QwenRole role = message.getRole();
                        String content = message.getContent();
                        Assert.assertNotNull(role);
                        Assert.assertFalse(content.isEmpty());
                        getLogger().info(role + " | " + content + " | " + choice.getFinishReason());
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
