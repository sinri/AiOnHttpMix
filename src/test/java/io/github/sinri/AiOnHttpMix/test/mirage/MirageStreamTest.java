package io.github.sinri.AiOnHttpMix.test.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMRole;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMSimpleRoleMessagePair;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentType;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MirageStreamTest extends MirageTestBase {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    private MirageRequestEntity buildMirageRequestEntity() {
        return new MirageRequestEntity()
                .addToPrompt(new AnyLLMSimpleRoleMessagePair(AnyLLMRole.system, "你是一个专业的程序员。"))
                .addToPrompt(new AnyLLMSimpleRoleMessagePair(AnyLLMRole.user, "请帮我执行一行python计算代码 `35/5.0-1`，给我结果。"));
    }

    @Test
    public void test1() {
        KeelAsyncKit.pseudoAwait(promise -> {
            getMirageSDK().requestStream(
                            "QwenPlus",
                            null,
                            true,
                            buildMirageRequestEntity(),
                            180_000L,
                            s -> {
                                getLogger().info("CHUNK | " + s);
                            }
                    )
                    .compose(anyLLMResponse -> {
//                    getLogger().info(anyLLMResponse.toString());
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void test2() {
        var req = buildMirageRequestEntity()
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
        getLogger().info("REQ:", req.toJsonObject());
        KeelAsyncKit.pseudoAwait(promise -> {
            getMirageSDK().requestStream(
                            "QwenPlus",
                            null,
                            true,
                            req,
                            180_000L,
                            s -> {
                                getLogger().info("CHUNK | " + s);
                            }
                    )
                    .compose(anyLLMResponse -> {
//                    getLogger().info(anyLLMResponse.toString());
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }
}
