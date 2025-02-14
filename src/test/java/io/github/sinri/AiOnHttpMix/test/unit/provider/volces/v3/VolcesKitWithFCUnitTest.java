package io.github.sinri.AiOnHttpMix.test.unit.provider.volces.v3;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.test.unit.core.LLMUnitTestCoverageForFC;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.AiOnHttpMix.volces.v3.chunk.VolcesChatResponseChunk;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatFunctionCallForRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatMessageForRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatToolDefinition;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatMessageToolCallForResponse;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseChoice;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseMessage;
import io.github.sinri.AiOnHttpMix.volces.v3.tool.VolcesChatFunctionDefinition;
import io.vertx.core.Future;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class VolcesKitWithFCUnitTest extends AbstractVolcesKitTestUnit
        implements LLMUnitTestCoverageForFC<VolcesChatRequest> {
    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testSyncWithToolCall() {
        this.async(() -> getKit()
                .chat(getServiceMeta(), generateRequest(), generateRequestId())
                .compose(resp -> {
                    List<VolcesChatResponseChoice> choices = resp.getChoices();
                    VolcesChatResponseChoice choice = choices.get(0);
                    VolcesChatResponseMessage message = choice.getMessage();
                    VolcesChatRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);

                    List<VolcesChatMessageToolCallForResponse> toolCalls = message.getToolCalls();
                    assert toolCalls != null;
                    Assert.assertFalse(toolCalls.isEmpty());
                    for (VolcesChatMessageToolCallForResponse toolCall : toolCalls) {
                        VolcesChatFunctionCallForRequest function = toolCall.getFunction();
                        assert function != null;
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
        this.async(() -> getKit()
                .chatStreamWithChunkHandler(getServiceMeta(), generateRequest(), chunk -> {
                    List<VolcesChatResponseChunk.StreamChoice> choices = chunk.getChoices();
                    if (choices != null) {
                        VolcesChatResponseChunk.StreamChoice choice = choices.get(0);
                        VolcesChatResponseChunk.ChoiceDelta delta = choice.getDelta();
                        VolcesChatRole role = delta.getRole();
                        String content = delta.getContent();
                        getUnitTestLogger().info("From " + role + ": " + content);

                        List<VolcesChatResponseChunk.ChoiceDeltaToolCall> toolCalls = delta.getToolCalls();
                        if (toolCalls != null) {
                            VolcesChatResponseChunk.ChoiceDeltaToolCall toolCall = toolCalls.get(0);
                            VolcesChatResponseChunk.FunctionCallChunk function = toolCall.getFunction();
                            String name = function.getName();
                            String arguments = function.getArguments();
                            getUnitTestLogger().info("Function " + name + "(" + arguments + ")");
                        }
                    }
                }, 0, generateRequestId())
        );
    }

    @Test
    @TestPassed(time = "2025-02-13")
    @Override
    public void testStreamBufferWithToolCall() {
        AigcMix.enableVerboseLogger();
        this.async(() -> getKit()
                .chatStreamWithBuffer(getServiceMeta(), generateRequest(), 60, generateRequestId())
                .compose(resp -> {
                    List<VolcesChatResponseChoice> choices = resp.getChoices();
                    VolcesChatResponseChoice choice = choices.get(0);
                    VolcesChatResponseMessage message = choice.getMessage();
                    VolcesChatRole role = message.getRole();
                    String content = message.getContent();
                    getUnitTestLogger().info("From " + role + ": " + content);

                    List<VolcesChatMessageToolCallForResponse> toolCalls = message.getToolCalls();
                    assert toolCalls != null;
                    Assert.assertFalse(toolCalls.isEmpty());
                    for (VolcesChatMessageToolCallForResponse toolCall : toolCalls) {
                        VolcesChatFunctionCallForRequest function = toolCall.getFunction();
                        assert function != null;
                        String name = function.getName();
                        String arguments = function.getArguments();
                        getUnitTestLogger().info("Function " + name + "(" + arguments + ")");
                    }

                    return Future.succeededFuture();
                })
        );
        AigcMix.disableVerboseLogger();
    }

    @Override
    public VolcesChatRequest generateRequest() {
        return VolcesChatRequest
                .create()
                .addMessage(VolcesChatMessageForRequest.create()
                                                       .setRole(VolcesChatRole.system)
                                                       .setContent("你现在负责为大家搜寻数据集。你需要根据用户的描述，识别出可能的数据集关键词，据此查找相关的数据集。")
                )
                .addMessage(m -> m
                        .setRole(VolcesChatRole.user)
                        .setContent("每年在天猫平台上达成的商品销售额")
                )
                .addTool(VolcesChatToolDefinition.create(
                        VolcesChatFunctionDefinition.builder()
                                                    .functionName("searchDataSet")
                                                    .functionDescription("根据信息查询可能的数据集")
                                                    .propertyAsString("keywords", "由一组关键字字符串组成的JSON数组")
                                                    .build()
                ));
    }

    @Override
    protected String getServiceName() {
        return "doubao-pro-128k";
    }
}
