package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponse;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponse;

import java.util.ArrayList;
import java.util.List;

public interface AnyLLMResponse {

    static AnyLLMResponse from(OpenAIChatGptResponse openAIChatGptResponse) {
        //Keel.getLogger().fatal("FROM OpenAIChatGptResponse: ", openAIChatGptResponse.cloneAsJsonObject());
        List<OpenAIChatGptResponseChoice> choices = openAIChatGptResponse.getChoices();
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        List<AnyLLMResponseChoice> anyLLMResponseChoices = new ArrayList<>();
        for (OpenAIChatGptResponseChoice choice : choices) {
            //Keel.getLogger().fatal("FROM OpenAIChatGptResponse.choices[].choice: ", choice.cloneAsJsonObject());

            String finishReason = choice.getFinishReason();
            AssistantMessage message = choice.getMessage();
            String content = message.getContent();
            List<OpenAIChatGptResponseToolCall> toolCalls = message.getToolCalls();
            List<AnyLLMResponseToolFunctionCall> functionCalls = new ArrayList<>();
            if (toolCalls != null && !toolCalls.isEmpty()) {
                for (OpenAIChatGptResponseToolCall toolCall : toolCalls) {
                    OpenAIChatGptResponseFunctionCall function = toolCall.getFunction();
                    if (function != null) {
                        String name = function.getName();
                        String arguments = function.getArguments();

                        AnyLLMResponseToolFunctionCall fc = AnyLLMResponseToolFunctionCall.build(name, arguments);
                        functionCalls.add(fc);
                    }
                }
            }

            AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(finishReason, content, functionCalls);
            anyLLMResponseChoices.add(anyLLMResponseChoice);
            //Keel.getLogger().fatal("FROM OpenAIChatGptResponse.choices[].choice added to "+anyLLMResponseChoices.size());
        }

        return new AnyLLMResponseImpl(anyLLMResponseChoices);
    }

    static AnyLLMResponse from(QwenResponseInMessageFormat qwenResponseInMessageFormat) {
        return null;//todo
    }

    static AnyLLMResponse from(VolcesChatResponse volcesChatResponse) {
        return null;//todo
    }

    List<AnyLLMResponseChoice> getChoices();
}
