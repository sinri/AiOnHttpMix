package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class MixQwenTest extends MixTestCore {


    @Override
    protected @NotNull Future<Void> starting() {
        return super.starting()
                .compose(v -> {
                    anyLLMKit = new AnyLLMKit().useQwen(getMirageSDK());
                    return Future.succeededFuture();
                });
    }

    @TestUnit(skip = true)
    @Override
    public Future<Void> pureStream() {
        return super.pureStream();
    }

    @TestUnit(skip = true)
    @Override
    public Future<Void> pureNonStream() {
        return super.pureNonStream();
    }

    @TestUnit(skip = true)
    @Override
    public Future<Void> fcNonStream() {
        return super.fcNonStream();
    }

    @TestUnit(skip = true)
    @Override
    public Future<Void> fcStream() {
        return super.fcStream();
    }

    @TestUnit(skip = true)
    @Override
    public Future<Void> mixFcNonStream() {
        return super.mixFcNonStream();
    }

    @TestUnit(skip = true)
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

    @TestUnit(skip = false)
    public Future<Void> pureNonStream3() {
        return getAnyLLMKit()
                .request(anyLLMRequest -> {
                    anyLLMRequest
                            .addSystemMessage("你是一个专业的程序员。")
                            .addUserMessage("本日，美国人民党在大选中获胜。"
                                    + "\n提取上面这句话中的主谓宾，输出一个JSON对象文本，不要有多余内容。输出示例如下:\n" + (new JsonObject()
                                    .put("主语", "我")
                                    .put("谓语", "吃")
                                    .put("宾语", "饭")
                            ));
                })
                .compose(anyLLMResponse -> {
                    getLogger().info("RESP", anyLLMResponse.toJsonObject());
                    getLogger().info("CONTENT: " + anyLLMResponse.getChoices().get(0).getContent());
                    return Future.succeededFuture();
                });
    }
}
