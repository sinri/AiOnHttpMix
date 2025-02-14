package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenModel;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenResponseChunk;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolCall;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolDefinition;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForFC;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QwenKitChatWithFCUnitTest extends AbstractQwenKitUnitTest
        implements LLMUnitTestCoverageForFC<QwenRequest> {
    @Override
    public void setUp() {
        super.setUp();
        //        AigcMix.enableVerboseLogger();
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithToolCall() {
        this.async(() -> getKit()
                .chatForMessageResponse(
                        getServiceMeta(),
                        generateRequest(),
                        generateRequestId()
                )
                .compose(resp -> {
                    QwenResponseInMessageFormat.OutputForMessageResponse output = resp.getOutput();
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = output.getChoices();
                    assert choices != null;
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    QwenMessage message = choice.getMessage();
                    QwenRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);
                    List<QwenToolCall> toolCalls = message.getToolCalls();
                    assert toolCalls != null;
                    Assert.assertFalse(toolCalls.isEmpty());
                    for (QwenToolCall toolCall : toolCalls) {
                        QwenToolCall.FunctionCall function = toolCall.getFunction();
                        String name = function.getName();
                        String arguments = function.getArguments();
                        getUnitTestLogger().info("Function " + name + "(" + arguments + ")");
                    }
                    return Future.succeededFuture();
                })
        );
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamWithToolCall() {
        //AigcMix.enableVerboseLogger();
        this.async(() -> getKit()
                .chatStreamWithChunkHandler(
                        getServiceMeta(),
                        generateRequest().handleParameters(p -> p.setIncrementalOutput(true)),
                        chunk -> {
                            QwenResponseChunk.OutputChunkForMessageResponse output = chunk.getOutput();
                            List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
                            QwenResponseChunk.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                            QwenMessage message = choice.getMessage();
                            QwenRole role = message.getRole();
                            String content = message.getContent();

                            getUnitTestLogger().info("From " + role + ": " + content);

                            List<QwenToolCall> toolCalls = message.getToolCalls();
                            if (toolCalls != null) {
                                for (QwenToolCall toolCall : toolCalls) {
                                    QwenToolCall.FunctionCall function = toolCall.getFunction();
                                    String name = function.getName();
                                    String arguments = function.getArguments();
                                    getUnitTestLogger().info("Function " + name + "(" + arguments + ")");
                                }
                            }
                        },
                        0,
                        generateRequestId()
                ));
        //AigcMix.disableVerboseLogger();
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamBufferWithToolCall() {
        this.async(() -> getKit()
                .chatStreamWithBuffer(
                        getServiceMeta(),
                        generateRequest().handleParameters(p -> p.setIncrementalOutput(true)),
                        0,
                        generateRequestId()
                )
                .compose(resp -> {
                    QwenResponseInMessageFormat.OutputForMessageResponse output = resp.getOutput();
                    List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = output.getChoices();
                    assert choices != null;
                    QwenResponseInMessageFormat.OutputForMessageResponse.Choice choice = choices.get(0);
                    QwenMessage message = choice.getMessage();
                    QwenRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);
                    List<QwenToolCall> toolCalls = message.getToolCalls();
                    assert toolCalls != null;
                    Assert.assertFalse(toolCalls.isEmpty());
                    for (QwenToolCall toolCall : toolCalls) {
                        QwenToolCall.FunctionCall function = toolCall.getFunction();
                        String name = function.getName();
                        String arguments = function.getArguments();
                        getUnitTestLogger().info("Function " + name + "(" + arguments + ")");
                    }
                    return Future.succeededFuture();
                })
        );
    }

    @Override
    public QwenRequest generateRequest() {
        return QwenRequest
                .create()
                .setModel(QwenModel.QWEN_PLUS.getModelCode())
                .handleInput(input -> input
                        .addSystemMessage("你是杭州的一家公司的行政人员，负责安排高层人员行程。")
                        .addUserMessage("2025年春节老板在泉州鲤城区西街有个考察访问活动，打算提前一天抵达，请安排行程并准备提醒事项。")
                )
                .handleParameters(p -> p
                        .addTool(QwenToolDefinition.asFunction(
                                        "travel_scheduler",
                                        "schedule travel schedule and traffic",
                                        List.of(
                                                new QwenToolDefinition.FunctionArgument(
                                                        "from",
                                                        "string",
                                                        "the city to set off",
                                                        true
                                                ),
                                                new QwenToolDefinition.FunctionArgument(
                                                        "to",
                                                        "string",
                                                        "the city as the destination",
                                                        true
                                                ),
                                                new QwenToolDefinition.FunctionArgument(
                                                        "set_off_day",
                                                        "string",
                                                        "the day to set off, in YYYY-MM-DD format",
                                                        true
                                                )
                                        )
                                )
                        )
                );
    }
}
