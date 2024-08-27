package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.OpenAIChatGptMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface OpenAIChatGptResponseChunkChoiceDelta extends UnmodifiableJsonifiableEntity {
    static OpenAIChatGptResponseChunkChoiceDelta wrap(JsonObject jsonObject) {
        return new OpenAIChatGptResponseChunkChoiceDeltaImpl(jsonObject);
    }

    @Nullable
    default String getContentAsText() {
        return readString("content");
    }

    @Nullable
    default OpenAIChatGptMessage.ChatCompletionRequestMessageRole getRole() {
        String role = readString("role");
        if (role == null) return null;
        return OpenAIChatGptMessage.ChatCompletionRequestMessageRole.valueOf(role);
    }

    @Nullable
    default List<OpenAIChatGptResponseToolCall> getToolCalls() {
        List<OpenAIChatGptResponseToolCall> toolCalls = new ArrayList<>();
        List<JsonObject> array = readJsonObjectArray("tool_calls");
        if (array == null) return null;
        for (JsonObject x : array) {
            OpenAIChatGptResponseToolCall toolCall = OpenAIChatGptResponseToolCall.wrapForChunk(x);
            toolCalls.add(toolCall);
        }
        return toolCalls;
    }


//        @Nullable
//        default List<OpenAIToolCallMixin.FunctionCallInChunk> getFunctionCalls() {
//            List<OpenAIToolCallMixin.FunctionCallInChunk> functionCalls = new ArrayList<>();
//            List<JsonObject> array = readJsonObjectArray("function_calls");
//            if (array == null) return null;
//            for (JsonObject x : array) {
//                FunctionCallInChunkImpl functionCallInChunk = new FunctionCallInChunkImpl(x);
//                functionCalls.add(functionCallInChunk);
//            }
//            return functionCalls;
//        }
}
