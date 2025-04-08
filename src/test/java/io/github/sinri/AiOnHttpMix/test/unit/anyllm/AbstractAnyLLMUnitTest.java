package io.github.sinri.AiOnHttpMix.test.unit.anyllm;

import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMResponseChoice;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMResponseToolFunctionCall;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyUnitTest;
import io.vertx.core.Future;
import org.junit.Assert;

import java.util.List;

public abstract class AbstractAnyLLMUnitTest extends AnyUnitTest
        implements AnyLLMUnitTestCommonMixin, AnyLLMUnitTestWithoutFCMixin, AnyLLMUnitTestWithFCMixin {
    @Override
    public AnyLLMRequest generateRequestWithoutToolCall() {
        return AnyLLMRequest.create()
                            .addSystemMessage("你是个数学大师")
                            .addUserMessage("如何定义i？");
    }

    @Override
    public void testSyncWithoutToolCall() {
        async(() -> this.withAnyLLM(anyLLMKit -> anyLLMKit
                .request(generateRequestWithoutToolCall())
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    AnyLLMResponseChoice anyLLMResponseChoice = choices.get(0);
                    String content = anyLLMResponseChoice.getContent();
                    Assert.assertNotNull(content);
                    getUnitTestLogger().info("content: " + content);
                    String reasoningContent = anyLLMResponseChoice.getReasoningContent();
                    getUnitTestLogger().info("reasoningContent: " + reasoningContent);
                    return Future.succeededFuture();
                })));
    }

    @Override
    public void testStreamBufferWithoutToolCall() {
        async(() -> this.withAnyLLM(anyLLMKit -> anyLLMKit
                .requestWithStreamBuffer(generateRequestWithoutToolCall())
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    AnyLLMResponseChoice anyLLMResponseChoice = choices.get(0);
                    String content = anyLLMResponseChoice.getContent();
                    Assert.assertNotNull(content);
                    getUnitTestLogger().info("content: " + content);
                    String reasoningContent = anyLLMResponseChoice.getReasoningContent();
                    getUnitTestLogger().info("reasoningContent: " + reasoningContent);
                    return Future.succeededFuture();
                })));
    }

    @Override
    public AnyLLMRequest generateRequestWithToolCall() {
        return AnyLLMRequest.create()
                            .addSystemMessage("你是一个仓储管理员")
                            .addUserMessage("仓库里现在货号为88883333的菜刀还有多少量？")
                            .addFunctionToolDefinition(builder -> builder
                                    .functionName("inventory_query")
                                    .functionDescription("query the number of a certain product in inventory")
                                    .propertyAsString("product_id", "The Product ID")
                            );
    }

    @Override
    public void testSyncWithToolCall() {
        async(() -> this.withAnyLLM(anyLLMKit -> anyLLMKit
                .request(generateRequestWithToolCall())
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    AnyLLMResponseChoice anyLLMResponseChoice = choices.get(0);
                    String content = anyLLMResponseChoice.getContent();
                    getUnitTestLogger().info("content: " + content);
                    List<AnyLLMResponseToolFunctionCall> functionCalls = anyLLMResponseChoice.getFunctionCalls();
                    AnyLLMResponseToolFunctionCall anyLLMResponseToolFunctionCall = functionCalls.get(0);
                    String functionName = anyLLMResponseToolFunctionCall.getFunctionName();
                    String functionArguments = anyLLMResponseToolFunctionCall.getFunctionArguments();
                    getUnitTestLogger().info("functionName: " + functionName);
                    getUnitTestLogger().info("functionArguments: " + functionArguments);
                    return Future.succeededFuture();
                })
        ));
    }

    @Override
    public void testStreamBufferWithToolCall() {
        async(() -> this.withAnyLLM(anyLLMKit -> anyLLMKit
                .requestWithStreamBuffer(generateRequestWithToolCall())
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    AnyLLMResponseChoice anyLLMResponseChoice = choices.get(0);
                    String content = anyLLMResponseChoice.getContent();
                    getUnitTestLogger().info("content: " + content);
                    List<AnyLLMResponseToolFunctionCall> functionCalls = anyLLMResponseChoice.getFunctionCalls();
                    AnyLLMResponseToolFunctionCall anyLLMResponseToolFunctionCall = functionCalls.get(0);
                    String functionName = anyLLMResponseToolFunctionCall.getFunctionName();
                    String functionArguments = anyLLMResponseToolFunctionCall.getFunctionArguments();
                    getUnitTestLogger().info("functionName: " + functionName);
                    getUnitTestLogger().info("functionArguments: " + functionArguments);
                    return Future.succeededFuture();
                })
        ));
    }
}
