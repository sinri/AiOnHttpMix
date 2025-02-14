package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponseChoice;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponseToolFunctionCall;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMSimpleRoleMessagePair;
import io.github.sinri.AiOnHttpMix.test.unit.core.AnyUnitTest;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentType;
import io.vertx.core.Future;
import org.junit.Assert;

import java.util.List;

public abstract class AnyMirageUnitTest extends AnyUnitTest
        implements AnyMirageUnitTestWithoutFCMixin, AnyMirageUnitTestWithFCMixin {

    @Override
    public MirageRequestEntity generateRequestWithoutFC() {
        return new MirageRequestEntity()
                .addToPrompt(new AnyLLMSimpleRoleMessagePair(
                        AnyLLMRole.system,
                        "你是一个专业的程序员。"
                ))
                .addToPrompt(new AnyLLMSimpleRoleMessagePair(
                        AnyLLMRole.user,
                        "请帮我执行一行python计算代码 `35/5.0-1`，给我结果。"
                ));
    }

    @Override
    public void testSyncWithoutFC() {
        async(() -> withMirage(mirageSDK -> mirageSDK
                .requestSync(
                        getModel(),
                        getService(),
                        true,
                        generateRequestWithoutFC()
                )
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    AnyLLMResponseChoice anyLLMResponseChoice = choices.get(0);
                    String content = anyLLMResponseChoice.getContent();
                    getUnitTestLogger().info("content: " + content);
                    Assert.assertNotNull(content);
                    return Future.succeededFuture();
                })));
    }

    @Override
    public void testStreamWithoutFC() {
        async(() -> withMirage(mirageSDK -> mirageSDK
                .requestStream(
                        getModel(),
                        getService(),
                        true,
                        generateRequestWithoutFC(),
                        s -> getUnitTestLogger().info("chunk: " + s)
                )));
    }

    @Override
    public MirageRequestEntity generateRequestWithFC() {
        return new MirageRequestEntity()
                .addToPrompt(new AnyLLMSimpleRoleMessagePair(
                        AnyLLMRole.system,
                        "你是一个专业的程序员。"
                ))
                .addToPrompt(new AnyLLMSimpleRoleMessagePair(
                        AnyLLMRole.user,
                        "请帮我执行一行python计算代码 `35/5.0-1`，给我结果。"
                ))
                .addFunctionDefinition(
                        "python_code_runner",
                        "执行给定的Python代码并给出结果",
                        List.of(
                                new FunctionToolArgumentDefinition(
                                        FunctionToolArgumentType.STRING,
                                        "code",
                                        "要执行的python代码"
                                )
                        )
                );
    }

    @Override
    public void testSyncWithFC() {
        async(() -> withMirage(mirageSDK -> mirageSDK
                .requestSync(
                        getModel(),
                        getService(),
                        true,
                        generateRequestWithFC()
                )
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
                })));
    }

    @Override
    public void testStreamWithFC() {
        async(() -> withMirage(mirageSDK -> mirageSDK
                .requestStream(
                        getModel(),
                        getService(),
                        true,
                        generateRequestWithFC(),
                        s -> getUnitTestLogger().info("chunk: " + s)
                )));
    }
}
