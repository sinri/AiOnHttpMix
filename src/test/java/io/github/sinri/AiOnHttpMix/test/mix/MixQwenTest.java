package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Before;
import org.junit.Test;

public class MixQwenTest extends MixTestCore {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        anyLLMKit = new AnyLLMKit().useDataScope(getMirageSDK());
    }

    @Test
    public void testPureStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            pureStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testPureNonStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            pureNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testFcNonStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            fcNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testFcStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            fcStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testMixFcNonStream() {
        KeelAsyncKit.pseudoAwait(promise -> {
            mixFcNonStream()
                    .compose(v -> {
                        return Future.succeededFuture();
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testMixFcNonStream2() {
        KeelAsyncKit.pseudoAwait(promise -> {
            getAnyLLMKit()
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
                    })
                    .onComplete(promise);
        });
    }

    @Test
    public void testPureNonStream3() {
        KeelAsyncKit.pseudoAwait(promise -> {
            getAnyLLMKit()
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
                    })
                    .onComplete(promise);
        });
    }
}
