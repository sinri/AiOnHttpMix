package io.github.sinri.AiOnHttpMix.test.unit.anyllm;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.mix.FunctionCallAdapter;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMRequest;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMResponseChoice;
import io.github.sinri.AiOnHttpMix.mix.chat.AnyLLMResponseToolFunctionCall;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyUnitTest;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentType;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import java.util.List;

public abstract class AbstractAnyLLMUnitTest extends AnyUnitTest
        implements AnyLLMUnitTestCommonMixin, AnyLLMUnitTestWithoutFCMixin, AnyLLMUnitTestWithFCMixin {
    @Override
    public AnyLLMRequest generateRequestWithoutToolCall() {
        return AnyLLMRequest.create()
                            .addRoleMessage(AnyLLMRole.system, "你是个数学大师")
                            .addRoleMessage(AnyLLMRole.user, "如何定义i？");
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
        var fca = new InventoryQueryFCA();
        return AnyLLMRequest.create()
                            .addRoleMessage(AnyLLMRole.system, "你是一个仓储管理员")
                            .addRoleMessage(AnyLLMRole.user, "仓库里现在货号为88883333的菜刀还有多少量？")
                            .addFunctionToolDefinition(fca);
    }

    @Override
    public void testSyncWithToolCall() {
        async(() -> this.withAnyLLM(anyLLMKit -> anyLLMKit
                .request(generateRequestWithToolCall())
                .compose(anyLLMResponse -> {
                    getUnitTestLogger().info("anyLLMResponse", anyLLMResponse.toJsonObject());
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
                    try {
                        JsonObject parsed = new JsonObject(functionArguments);
                        getUnitTestLogger().info("Parsed functionArguments: ", parsed);
                    } catch (Throwable throwable) {
                        //                        getUnitTestLogger().exception(throwable);
                        Assert.fail(throwable.getMessage());
                    }
                    return Future.succeededFuture();
                })
        ));
    }

    protected static class InventoryQueryFCA implements FunctionCallAdapter {
        @Override
        public @NotNull String getFunctionName() {
            return "inventory_query";
        }

        @Override
        public @NotNull String getFunctionDescription() {
            return "query the number of a certain product in inventory";
        }

        @Override
        public @NotNull List<FunctionToolArgumentDefinition> getArguments() {
            return List.of(
                    new FunctionToolArgumentDefinition(
                            FunctionToolArgumentType.INTEGER,
                            "product_id", "The Product ID"
                    )
            );
        }

        @Override
        public @NotNull Future<String> callFunction(@Nullable JsonObject arguments, @Nullable JsonObject fixedArgument) {
            return Future.succeededFuture(String.valueOf(1123L));
        }

    }
}
