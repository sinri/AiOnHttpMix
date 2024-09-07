package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponse;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolCall;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatFunctionCallForRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatMessageToolCallForResponse;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponse;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseChoice;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseMessage;

import java.util.ArrayList;
import java.util.List;

public interface AnyLLMResponse {
    static AnyLLMResponse from(AssistantMessage assistantMessage) {
        List<AnyLLMResponseChoice> anyLLMResponseChoices = new ArrayList<>();

        List<OpenAIChatGptResponseToolCall> toolCalls = assistantMessage.getToolCalls();
        List<AnyLLMResponseToolFunctionCall> functionCalls = new ArrayList<>();
        if (toolCalls != null) {
            toolCalls.forEach(toolCall -> {
                OpenAIChatGptResponseFunctionCall function = toolCall.getFunction();
                if (function != null) {
                    String name = function.getName();
                    String arguments = function.getArguments();

                    AnyLLMResponseToolFunctionCall fc = AnyLLMResponseToolFunctionCall.build(name, arguments);
                    functionCalls.add(fc);
                }
            });
        }

        AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(null, assistantMessage.getContent(), functionCalls);
        anyLLMResponseChoices.add(anyLLMResponseChoice);
        return new AnyLLMResponseImpl(anyLLMResponseChoices);
    }

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
        QwenResponseInMessageFormat.OutputForMessageResponse output = qwenResponseInMessageFormat.getOutput();
        List<QwenResponseInMessageFormat.OutputForMessageResponse.Choice> choices = output.getChoices();
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        List<AnyLLMResponseChoice> anyLLMResponseChoices = new ArrayList<>();

        choices.forEach(choice -> {
            String finishReason = choice.getFinishReason();
            QwenMessage message = choice.getMessage();
            String content = message.getContent();
            List<QwenToolCall> toolCalls = message.getToolCalls();
            List<AnyLLMResponseToolFunctionCall> functionCalls = new ArrayList<>();
            if (toolCalls != null && !toolCalls.isEmpty()) {
                for (QwenToolCall toolCall : toolCalls) {
                    QwenToolCall.FunctionCall function = toolCall.getFunction();
                    String name = function.getName();
                    String arguments = function.getArguments();
                    AnyLLMResponseToolFunctionCall functionCall = AnyLLMResponseToolFunctionCall.build(name, arguments);
                    functionCalls.add(functionCall);
                }
            }

            AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(finishReason, content, functionCalls);
            anyLLMResponseChoices.add(anyLLMResponseChoice);
        });

        return new AnyLLMResponseImpl(anyLLMResponseChoices);
    }

    static AnyLLMResponse from(VolcesChatResponse volcesChatResponse) {
        List<VolcesChatResponseChoice> choices = volcesChatResponse.getChoices();
        if (choices == null || choices.isEmpty()) {
            return null;
        }

        List<AnyLLMResponseChoice> anyLLMResponseChoices = new ArrayList<>();

        choices.forEach(choice -> {
            String finishReason = choice.getFinishReason();
            VolcesChatResponseMessage message = choice.getMessage();
            String content = message.getContent();
            List<VolcesChatMessageToolCallForResponse> toolCalls = message.getToolCalls();
            List<AnyLLMResponseToolFunctionCall> functionCalls = new ArrayList<>();
            if (toolCalls != null && !toolCalls.isEmpty()) {
                for (VolcesChatMessageToolCallForResponse toolCall : toolCalls) {
                    VolcesChatFunctionCallForRequest function = toolCall.getFunction();
                    if (function != null) {
                        String name = function.getName();
                        String arguments = function.getArguments();
                        AnyLLMResponseToolFunctionCall functionCall = AnyLLMResponseToolFunctionCall.build(name, arguments);
                        functionCalls.add(functionCall);
                    }
                }
            }

            AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(finishReason, content, functionCalls);
            anyLLMResponseChoices.add(anyLLMResponseChoice);
        });

        return new AnyLLMResponseImpl(anyLLMResponseChoices);
    }


    List<AnyLLMResponseChoice> getChoices();
}
