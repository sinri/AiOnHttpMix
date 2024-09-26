package io.github.sinri.AiOnHttpMix.mix;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.1.0
 */
public interface AnyLLMResponseChoice {
    static AnyLLMResponseChoice build(String finishReason, String content, List<AnyLLMResponseToolFunctionCall> functionCalls) {
        return new AnyLLMResponseChoiceImpl(finishReason, content, functionCalls);
    }

    /**
     * @param jsonObject
     * @return
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
        return build(finishReason, content, functionCalls);
    }

    String getFinishReason();

    String getContent();

    List<AnyLLMResponseToolFunctionCall> getFunctionCalls();

    /**
     * @since 1.1.1
     */
    default JsonObject toJsonObject() {
        JsonArray jsonArray = new JsonArray();
        List<AnyLLMResponseToolFunctionCall> functionCalls = getFunctionCalls();
        if (functionCalls != null) {
            functionCalls.forEach(functionCall -> {
                jsonArray.add(functionCall.toJsonObject());
            });
        }
        return new JsonObject()
                .put("finish_reason", getFinishReason())
                .put("content", getContent())
                .put("function_calls", jsonArray);
    }
}
