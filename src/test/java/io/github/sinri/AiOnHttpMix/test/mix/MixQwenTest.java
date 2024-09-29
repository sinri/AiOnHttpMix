package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MixQwenTest extends MixTestCore {


    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    String dashscopeApiKey = Keel.config("dashscope.api_key");

                    var serviceMeta = new DashscopeServiceMeta(dashscopeApiKey);
                    anyLLMKit = new AnyLLMKit()
                            .useQwen(serviceMeta, SupportedModel.QwenPlus)
                            .throughMirage(getMirageSDK())
                    ;

                    return Future.succeededFuture();
                });
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> pureStream() {
        return super.pureStream();
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> pureNonStream() {
        return super.pureNonStream();
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> fcNonStream() {
        return super.fcNonStream();
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> fcStream() {
        return super.fcStream();
    }

    @TestUnit(skip = false)
    @Override
    public Future<Void> mixFcNonStream() {
        return super.mixFcNonStream();
    }

    @TestUnit(skip = false)
    public Future<Void> mixFcNonStream2() {
        return getAnyLLMKit()
                .request(anyLLMRequest -> {
                    anyLLMRequest.addFunctionToolDefinition(builder -> builder
                                    .functionName("python_code_runner")
                                    .functionDescription("执行给定的Python代码并给出结果")
                                    .propertyAsString("code", "要执行的python代码")
                            )
                            .addSystemMessage("你是一个专业的程序员。")
                            .addUserMessage("请帮我执行一行python计算代码 `35/5.0-1`，给我结果。");
                })
                .compose(anyLLMResponse -> {
                    getLogger().info("RESP", anyLLMResponse.toJsonObject());
                    return Future.succeededFuture();
                });
    }
}
