package io.github.sinri.AiOnHttpMix.test.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageRequestEntity;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentType;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;

import java.util.List;

public class MirageStreamTest extends MirageTestBase {
    @TestUnit(skip = true)
    public Future<Void> test1() {
        return getMirageSDK().requestStream(
                        "QwenPlus",
                        null,
                        true,
                        new MirageRequestEntity()
                                .setSystemPrompt("你是一个专业的程序员。")
                                .addUserPrompt("请帮我执行一行python计算代码 `35/5.0-1`，给我结果。"),
                        180_000L,
                        s -> {
                            getLogger().info("CHUNK | " + s);
                        }
                )
                .compose(anyLLMResponse -> {
//                    getLogger().info(anyLLMResponse.toString());
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test2() {
        var req = new MirageRequestEntity()
                .setSystemPrompt("你是一个专业的程序员。")
                .addUserPrompt("请帮我执行一行python计算代码 `35/5.0-1`，给我结果。")
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
        return getMirageSDK().requestStream(
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
                });
    }
}
