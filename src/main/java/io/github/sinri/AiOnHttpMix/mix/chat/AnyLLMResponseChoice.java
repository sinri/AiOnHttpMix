package io.github.sinri.AiOnHttpMix.mix.chat;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.1.0
 */
public interface AnyLLMResponseChoice {
    static AnyLLMResponseChoice build(
            String finishReason,
            String content,
            List<AnyLLMResponseToolFunctionCall> functionCalls,
            @Nullable String reasoningContent
    ) {
        return new AnyLLMResponseChoiceImpl(finishReason, content, functionCalls, reasoningContent);
    }

    /**
     * @since 1.1.4
     */
    static AnyLLMResponseChoice wrap(JsonObject jsonObject) {
        String finishReason = jsonObject.getString("finish_reason");
        String content = jsonObject.getString("content");
        JsonArray fcArray = jsonObject.getJsonArray("function_calls");
        List<AnyLLMResponseToolFunctionCall> functionCalls = new ArrayList<>();
        if (fcArray != null) {
            fcArray.forEach(fc -> {
                if (fc instanceof JsonObject) {
                    var wrapped = AnyLLMResponseToolFunctionCall.wrap((JsonObject) fc);
                    functionCalls.add(wrapped);
                }
            });
        }
        String reasoningContent = jsonObject.getString("reasoning_content");
        return build(finishReason, content, functionCalls, reasoningContent);
    }

    @Nullable
    String getFinishReason();

    @Nullable
    String getContent();

    @NotNull
    List<AnyLLMResponseToolFunctionCall> getFunctionCalls();

    /**
     * @since 1.2.3
     */
    @Nullable
    String getReasoningContent();

    /**
     * @since 1.1.1
     */
    default JsonObject toJsonObject() {
        JsonArray jsonArray = new JsonArray();
        List<AnyLLMResponseToolFunctionCall> functionCalls = getFunctionCalls();
        functionCalls.forEach(functionCall -> {
            jsonArray.add(functionCall.toJsonObject());
        });
        return new JsonObject()
                .put("finish_reason", getFinishReason())
                .put("content", getContent())
                .put("function_calls", jsonArray);
    }
}
