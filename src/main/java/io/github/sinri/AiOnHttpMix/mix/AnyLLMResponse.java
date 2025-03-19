package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponse;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolCall;
import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.deepseek.chat.message.DeepseekMessageInResponse;
import io.github.sinri.AiOnHttpMix.volces.v3.request.VolcesChatFunctionCallForRequest;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatMessageToolCallForResponse;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponse;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseChoice;
import io.github.sinri.AiOnHttpMix.volces.v3.response.VolcesChatResponseMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a response from any LLM (Language Learning Model) that standardizes the output format across different LLM providers.
 * This interface allows for the creation of an LLM response object from various provider-specific response types, such as OpenAI, Qwen, and Volces.
 * The standardized response includes choices, each with a finish reason, content, function calls, and reasoning content if applicable.
 * @since 1.1.0
 */
public interface AnyLLMResponse {
    static @NotNull AnyLLMResponse from(OpenAIChatGptResponseChoice resp) {
        AssistantMessage assistantMessage = resp.getMessage();

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

        AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(
                resp.getFinishReason(),
                assistantMessage.getContent(),
                functionCalls,
                null
        );
        anyLLMResponseChoices.add(anyLLMResponseChoice);
        return new AnyLLMResponseImpl(anyLLMResponseChoices);
    }

    static @Nullable AnyLLMResponse from(OpenAIChatGptResponse openAIChatGptResponse) {
        List<OpenAIChatGptResponseChoice> choices = openAIChatGptResponse.getChoices();
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        List<AnyLLMResponseChoice> anyLLMResponseChoices = new ArrayList<>();
        for (OpenAIChatGptResponseChoice choice : choices) {
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

            AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(
                    finishReason,
                    content,
                    functionCalls,
                    null
            );
            anyLLMResponseChoices.add(anyLLMResponseChoice);
        }

        return new AnyLLMResponseImpl(anyLLMResponseChoices);
    }

    static @Nullable AnyLLMResponse from(QwenResponseInMessageFormat qwenResponseInMessageFormat) {
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

            String reasoningContent = message.getReasoningContent();
            AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(
                    finishReason,
                    content,
                    functionCalls,
                    reasoningContent
            );
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

            AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(
                    finishReason,
                    content,
                    functionCalls,
                    null
            );
            anyLLMResponseChoices.add(anyLLMResponseChoice);
        });

        return new AnyLLMResponseImpl(anyLLMResponseChoices);
    }

    /**
     * @since 1.1.12
     */
    static AnyLLMResponse from(DeepseekChatResponse volcesChatResponse) {
        List<DeepseekChatResponse.Choice> choices = volcesChatResponse.getChoices();
        if (choices == null || choices.isEmpty()) {
            return null;
        }

        List<AnyLLMResponseChoice> anyLLMResponseChoices = new ArrayList<>();
        choices.forEach(choice -> {
            String finishReason = choice.getFinishReason();
            DeepseekMessageInResponse message = choice.getMessage();
            String content = message.getContent();

            List<AnyLLMResponseToolFunctionCall> functionCalls = new ArrayList<>();

            // FC of DeepSeek is not tested; R1 is not supported FC.
            var toolCalls = message.getToolCalls();
            if (toolCalls != null && !toolCalls.isEmpty()) {
                for (var toolCall : toolCalls) {
                    var function = toolCall.getFunction();
                    if (function != null) {
                        String name = function.getName();
                        String arguments = function.getArguments();
                        AnyLLMResponseToolFunctionCall functionCall = AnyLLMResponseToolFunctionCall.build(name, arguments);
                        functionCalls.add(functionCall);
                    }
                }
            }

            String reasoningContent = message.getReasoningContent();

            AnyLLMResponseChoice anyLLMResponseChoice = AnyLLMResponseChoice.build(
                    finishReason,
                    content,
                    functionCalls,
                    reasoningContent
            );
            anyLLMResponseChoices.add(anyLLMResponseChoice);
        });

        return new AnyLLMResponseImpl(anyLLMResponseChoices);
    }

    /**
     * @since 1.1.4
     */
    static AnyLLMResponse wrap(JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.getJsonArray("choices");
        List<AnyLLMResponseChoice> choices = new ArrayList<>();
        jsonArray.forEach(item -> {
            if (item instanceof JsonObject) {
                var c = AnyLLMResponseChoice.wrap((JsonObject) item);
                choices.add(c);
            }
        });
        return new AnyLLMResponseImpl(choices);
    }


    List<AnyLLMResponseChoice> getChoices();

    /**
     * @since 1.1.1
     */
    default JsonObject toJsonObject() {
        JsonArray array = new JsonArray();
        getChoices().forEach(c -> {
            array.add(c.toJsonObject());
        });
        return new JsonObject()
                .put("choices", array);
    }


}
