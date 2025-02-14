package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenVLModel;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.vl.*;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForNonFC;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.util.List;

public class QwenKitVLUnitTest extends AbstractQwenKitUnitTest implements LLMUnitTestCoverageForNonFC<QwenVLRequest> {
    @Override
    public QwenVLRequest generateRequest() {
        return QwenVLRequest
                .create()
                .setModel(QwenVLModel.QWEN_VL_PLUS.getModelCode())
                .setInput(QwenVLRequest.Input
                        .create()
                        .addMessage(QwenVLInputMessage
                                .create()
                                .setRole(QwenVLRole.user)
                                .addContentItem(QwenVLMessageContentItem
                                        .create()
                                        .setImage("https://pics6.baidu.com/feed/8601a18b87d6277f6438cea9e7199b3ee924fc2e.jpeg@f_auto?token=cb586c2ff83aefa74a3365453f55eaba")
                                )
                                .addContentItem(QwenVLMessageContentItem
                                        .create()
                                        .setText("图片中的人是谁，背景中的旗帜是什么？")
                                )
                        ))
                .handleParameters(p -> p.setIncrementalOutput(true));
    }


    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithoutToolCall() {
        this.async(() -> getKit()
                .chatVL(
                        getServiceMeta(),
                        generateRequest(),
                        generateRequestId()
                )
                .compose(resp -> {
                    //getLogger().info("resp");
                    List<QwenVLResponse.Choice> choices = resp.getOutput().getChoices();
                    QwenVLResponse.Choice choice = choices.get(0);
                    QwenVLOutputMessage message = choice.getMessage();
                    QwenVLRole role = message.getRole();
                    String finishReason = choice.getFinishReason();
                    getUnitTestLogger().info("Role: " + role + " | Finish Reason: " + finishReason);
                    List<QwenVLMessageContentItem> content = message.getContent();
                    content.forEach(item -> {
                        getUnitTestLogger().info("Content Item", new JsonObject()
                                .put("text", item.getText())
                                .put("image", item.getImage())
                        );
                    });

                    return Future.succeededFuture();
                }));
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamWithoutToolCall() {
        this.async(() -> getKit()
                .chatVLStreamWithChunkHandler(
                        getServiceMeta(),
                        generateRequest(),
                        chunk -> {
                            QwenVLResponse.Output output = chunk.getOutput();
                            if (output != null) {
                                List<QwenVLResponse.Choice> choices = output.getChoices();
                                if (choices != null) {
                                    QwenVLResponse.Choice choice = choices.get(0);
                                    QwenVLOutputMessage message = choice.getMessage();
                                    QwenVLRole role = message.getRole();
                                    List<QwenVLMessageContentItem> content = message.getContent();
                                    getUnitTestLogger().info("Role: " + role + " | content: " + content);
                                    content.forEach(item -> {
                                        String text = item.getText();
                                        String image = item.getImage();
                                        getUnitTestLogger().info("Text: " + text + " | Image: " + image);
                                    });
                                }
                            }
                        },
                        0,
                        generateRequestId()
                )
        );
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamBufferWithoutToolCall() {
        this.async(() -> getKit()
                .chatVLStreamWithBuffer(
                        getServiceMeta(),
                        generateRequest(),
                        0,
                        generateRequestId()
                )
                .compose(resp -> {
                    //getLogger().info("resp");
                    List<QwenVLResponse.Choice> choices = resp.getOutput().getChoices();
                    QwenVLResponse.Choice choice = choices.get(0);
                    QwenVLOutputMessage message = choice.getMessage();
                    QwenVLRole role = message.getRole();
                    String finishReason = choice.getFinishReason();
                    getUnitTestLogger().info("Role: " + role + " | Finish Reason: " + finishReason);
                    List<QwenVLMessageContentItem> content = message.getContent();
                    content.forEach(item -> {
                        getUnitTestLogger().info("Content Item", new JsonObject()
                                .put("text", item.getText())
                                .put("image", item.getImage())
                        );
                    });

                    return Future.succeededFuture();
                }));
    }
}
