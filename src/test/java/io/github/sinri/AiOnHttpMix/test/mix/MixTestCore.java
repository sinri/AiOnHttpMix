package io.github.sinri.AiOnHttpMix.test.mix;

import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponseChoice;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponseToolFunctionCall;
import io.github.sinri.AiOnHttpMix.mix.FunctionCallAdapter;
import io.github.sinri.AiOnHttpMix.test.BaseUnitTest;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentType;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;

import java.util.List;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MixTestCore extends BaseUnitTest {
    protected AnyLLMKit anyLLMKit;
    private MirageSDK mirageSDK;

    protected AnyLLMKit getAnyLLMKit() {
        return anyLLMKit;
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        var domain = Keel.config("mirage.domain");
        var clientCode = Keel.config("mirage.client_code");
        var clientSecret = Keel.config("mirage.client_secret");

        mirageSDK = new MirageSDK(domain, clientCode, clientSecret);
    }

    protected MirageSDK getMirageSDK() {
        return mirageSDK;
    }

    public Future<Void> pureNonStream() {
        return getAnyLLMKit()
                .request(
                        anyLLMRequest -> anyLLMRequest
                                .addSystemMessage("你是一个日本旅游博主，最近刚开始学中文，准备向中国人民介绍如何在日本旅游。")
                                .addUserMessage("大佬，介绍下筑波有什么好玩的")
                )
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    getLogger().info("choices count: " + choices.size());
                    Assert.assertEquals(1, choices.size());
                    for (AnyLLMResponseChoice choice : choices) {
                        String finishReason = choice.getFinishReason();
                        String content = choice.getContent();
                        getLogger().info("Response Choice | " + content + " | " + finishReason);
                        Assert.assertNotNull(content);
                    }
                    return Future.succeededFuture();
                });
    }

    public Future<Void> fcNonStream() {
        return getAnyLLMKit()
                .request(
                        anyLLMRequest -> anyLLMRequest
                                .addFunctionToolDefinition(b -> b
                                        .functionName("translateFromJPtoCN")
                                        .functionDescription("将日语翻译成中文的函数，输入一段日语文本以得到翻译后的对应中文文本。")
                                        .propertyAsString("jp", "字符串。日语原文。")
                                )
                                .addSystemMessage("あなたは日本の旅行社の社員です。今、中国人に日本への旅を紹介し勧めを務めています。ただ、あなたは中国語が苦手です。")
                                .addUserMessage("大佬，介绍下筑波有什么好玩的")
                )
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    getLogger().info("choices count: " + choices.size());
                    Assert.assertEquals(1, choices.size());
                    for (AnyLLMResponseChoice choice : choices) {
                        String finishReason = choice.getFinishReason();
                        String content = choice.getContent();
                        getLogger().info("Response Choice | " + content + " | " + finishReason);
                        List<AnyLLMResponseToolFunctionCall> functionCalls = choice.getFunctionCalls();
                        Assert.assertNotNull(functionCalls);
                        functionCalls.forEach(functionCall -> {
                            getLogger().info("Function Call | " + functionCall.getFunctionName() + " | " + functionCall.getFunctionArguments());
                        });
                    }
                    return Future.succeededFuture();
                });
    }

    public Future<Void> pureStream() {
        return getAnyLLMKit()
                .requestWithStreamBuffer(anyLLMRequest -> anyLLMRequest
                        .addSystemMessage("你是一个日本旅游博主，最近刚开始学中文，准备向中国人民介绍如何在日本旅游。")
                        .addUserMessage("请介绍下筑波有什么好玩的地方，尽可能详细，形成一篇2000字以上的文案。")
                )
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    getLogger().info("choices count: " + choices.size());
                    Assert.assertEquals(1, choices.size());
                    for (AnyLLMResponseChoice choice : choices) {
                        String finishReason = choice.getFinishReason();
                        String content = choice.getContent();
                        getLogger().info("Response Choice | " + content + " | " + finishReason);
                    }
                    return Future.succeededFuture();
                });
    }

    public Future<Void> fcStream() {
        return getAnyLLMKit()
                .requestWithStreamBuffer(
                        anyLLMRequest -> anyLLMRequest
                                .addFunctionToolDefinition(b -> b
                                        .functionName("translateFromJPtoCN")
                                        .functionDescription("将日语翻译成中文的函数，输入一段日语文本以得到翻译后的对应中文文本。")
                                        .propertyAsString("jp", "字符串。日语原文。")
                                )
                                .addSystemMessage("あなたは日本の旅行社の社員です。今、中国人に日本への旅を紹介し勧めを務めています。ただ、あなたは中国語が苦手です。")
                                .addUserMessage("大佬，介绍下筑波有什么好玩的")
                )
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    getLogger().info("choices count: " + choices.size());
                    Assert.assertEquals(1, choices.size());
                    for (AnyLLMResponseChoice choice : choices) {
                        String finishReason = choice.getFinishReason();
                        String content = choice.getContent();
                        getLogger().info("Response Choice | " + content + " | " + finishReason);
                        List<AnyLLMResponseToolFunctionCall> functionCalls = choice.getFunctionCalls();
                        functionCalls.forEach(functionCall -> {
                            getLogger().info("Function Call | " + functionCall.getFunctionName() + " | " + functionCall.getFunctionArguments());
                        });
                    }
                    return Future.succeededFuture();
                });
    }

    public Future<Void> mixFcNonStream() {
        var f = new FunctionToCheckPrice();
        return getAnyLLMKit()
                .unregisterAllFunctions()
                .registerFunction(f)
                .request(r -> r
                        .addFunctionToolDefinition(f)
                        .addSystemMessage("你是一个网店的客服，应对客人的售前咨询。")
                        .addUserMessage("东方牌牛肉干怎么卖啊")
                )
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    getLogger().info("choices count: " + choices.size());
                    if (!choices.isEmpty()) {
                        AnyLLMResponseChoice choice = choices.get(0);
                        String finishReason = choice.getFinishReason();
                        String content = choice.getContent();
                        getLogger().info("Response Choice | " + content + " | " + finishReason);
                        List<AnyLLMResponseToolFunctionCall> functionCalls = choice.getFunctionCalls();

                        if (!functionCalls.isEmpty()) {
                            AnyLLMResponseToolFunctionCall functionCall = functionCalls.get(0);
                            getLogger().info("Function Call | " + functionCall.getFunctionName() + " | " + functionCall.getFunctionArguments());
                            FunctionCallAdapter registeredFunction = getAnyLLMKit().getRegisteredFunction(functionCall.getFunctionName());
                            if (registeredFunction != null) {
                                return registeredFunction.callFunction(new JsonObject(functionCall.getFunctionArguments()))
                                        .compose(result -> {
                                            getLogger().info("Function Call Result:" + result);
                                            return Future.succeededFuture();
                                        });
                            }
                        }
                    }
                    return Future.succeededFuture();
                });
    }

    public static class FunctionToCheckPrice implements FunctionCallAdapter {

        @Override
        public @NotNull String getFunctionName() {
            return "CheckPrice";
        }

        @Override
        public @NotNull String getFunctionDescription() {
            return "本函数用于按照给定的商品名称查找商品的价格。";
        }

        @Override
        public @NotNull List<FunctionToolArgumentDefinition> getArguments() {
            return List.of(
                    new FunctionToolArgumentDefinition(FunctionToolArgumentType.STRING, "product_name", "商品名称")
            );
        }

        @Override
        public @NotNull Future<Object> callFunction(JsonObject arguments) {
            String productName = arguments.getString("product_name");
            int x = productName.length();
            return Future.succeededFuture(x);
        }
    }
}
