package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.chunk.ToolCallInChunkImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter.ContentFilterChoiceResultsImpl;
import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter.PromptFilterResultsImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface OpenAIResponseChunkMixin extends UnmodifiableJsonifiableEntity {
    interface ChoiceInChunk extends UnmodifiableJsonifiableEntity {
        default ContentFilterChoiceResults getContentFilterResults() {
            JsonObject contentFilterResults = readJsonObject("content_filter_results");
            if (contentFilterResults == null) return null;
            return new ContentFilterChoiceResultsImpl(contentFilterResults);
        }

        @Nullable
        Delta getDelta();

        @Nullable
        default String getFinishReason() {
            return readString("finish_reason");
        }

        default Integer getIndex() {
            return readInteger("index");
        }
    }

    interface Delta extends UnmodifiableJsonifiableEntity {
        @Nullable
        default String getContentAsText() {
            return readString("content");
        }

        @Nullable
        default OpenAIMessageMixin.ChatCompletionRequestMessageRole getRole() {
            String role = readString("role");
            if (role == null) return null;
            return OpenAIMessageMixin.ChatCompletionRequestMessageRole.valueOf(role);
        }

        @Nullable
        default List<ChatGPTKit.ToolCallInChunk> getToolCalls() {
            List<ChatGPTKit.ToolCallInChunk> toolCalls = new ArrayList<>();
            List<JsonObject> array = readJsonObjectArray("tool_calls");
            if (array == null) return null;
            for (JsonObject x : array) {
                ToolCallInChunkImpl toolCall = new ToolCallInChunkImpl(x);
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

    default Integer getCreated() {
        return readInteger("created");
    }

    default String getId() {
        return readString("id");
    }

    default String getObject() {
        return readString("object");
    }

    @Nullable
    default String getModel() {
        return readString("model");
    }

    @Nullable
    default String getSystemFingerprint() {
        return readString("system_fingerprint");
    }

    /**
     * 一般存在于第一个Piece中。
     */
    @Nullable
    default List<PromptFilterResults> getPromptFilterResults() {
        var array = readJsonObjectArray("prompt_filter_results");
        if (array == null) return null;
        List<PromptFilterResults> list = new ArrayList<>();
        array.forEach(x -> {
            var y = new PromptFilterResultsImpl(x);
            list.add(y);
        });
        return list;
    }

    @NotNull
    List<ChoiceInChunk> getChoices();


    // logprobs
}
