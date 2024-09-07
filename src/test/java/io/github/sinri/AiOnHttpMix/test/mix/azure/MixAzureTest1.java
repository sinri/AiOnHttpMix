package io.github.sinri.AiOnHttpMix.test.mix.azure;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponseChoice;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponseToolFunctionCall;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;

import java.util.List;

public class MixAzureTest1 extends MixTestCoreWithAzure {
    @TestUnit(skip = true)
    public Future<Void> test1() {
        return getAnyLLMKit()
                .request(
                        anyLLMRequest -> anyLLMRequest
                                .addSystemMessage("你是一个日本旅游博主，最近刚开始学中文，准备向中国人民介绍如何在日本旅游。")
                                .addUserMessage("大佬，介绍下筑波有什么好玩的")
                )
                .compose(anyLLMResponse -> {
                    List<AnyLLMResponseChoice> choices = anyLLMResponse.getChoices();
                    getLogger().info("choices count: " + choices.size());
                    for (AnyLLMResponseChoice choice : choices) {
                        String finishReason = choice.getFinishReason();
                        String content = choice.getContent();
                        getLogger().info("Response Choice | " + content + " | " + finishReason);
                    }
                    return Future.succeededFuture();
                });
    }

    @TestUnit
    public Future<Void> test2() {
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
}
