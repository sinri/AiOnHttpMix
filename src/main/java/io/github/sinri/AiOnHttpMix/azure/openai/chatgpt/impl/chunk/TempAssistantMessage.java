package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIMessageMixin;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIToolCallMixin;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.message.AssistantMessage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Map;
import java.util.TreeMap;

public class TempAssistantMessage {
    private OpenAIMessageMixin.ChatCompletionRequestMessageRole role;
    private StringBuilder content;
    private final Map<Integer, TempToolCall> toolCallMap = new TreeMap<>();

    public TempAssistantMessage() {
    }

    public void acceptRole(OpenAIMessageMixin.ChatCompletionRequestMessageRole role) {
        this.role = role;
    }

    /**
     * @param toolCallInChunk First time met `tool_calls`.
     */
    public synchronized void acceptToolCall(ChatGPTKit.ToolCallInChunk toolCallInChunk) {
        TempToolCall tempToolCall = new TempToolCall(toolCallInChunk);
        this.toolCallMap.put(tempToolCall.getIndex(), tempToolCall);
    }

    public TempToolCall getTempToolCall(Integer index) {
        return this.toolCallMap.get(index);
    }

    public synchronized void acceptContentFragment(String fragment) {
        if (content == null) {
            content = new StringBuilder();
        }
        this.content.append(fragment);
    }

    public static class TempToolCall {
        private final String type;
        private final String id;
        private final Integer index;
        private TempFunctionCall function;

        public TempToolCall(ChatGPTKit.ToolCallInChunk toolCallInChunk) {
            this.type = toolCallInChunk.getType();
            this.id = toolCallInChunk.getId();
            this.index = toolCallInChunk.getIndex();
            OpenAIToolCallMixin.FunctionCall f = toolCallInChunk.getFunction();
            if (f != null) {
                this.function = new TempFunctionCall(f.getName());
                if (f.getArguments() != null) {
                    this.function.acceptArgumentFragment(f.getArguments());
                }
            } else {
                this.function = null;
            }
        }

        public Integer getIndex() {
            return index;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public TempFunctionCall getFunction() {
            return function;
        }

        public JsonObject toJsonObject() {
            return new JsonObject()
                    .put("type", type)
                    .put("id", id)
                    .put("index", index)
                    .put("function", function.toJsonObject());
        }
    }

    public static class TempFunctionCall {
        private final String functionName;
        private final StringBuilder functionArgument;

        public TempFunctionCall(String functionName) {
            this.functionName = functionName;
            this.functionArgument = new StringBuilder();
        }

        public String getFunctionName() {
            return functionName;
        }

        public synchronized void acceptArgumentFragment(String fragment) {
            this.functionArgument.append(fragment);
        }

        public JsonObject toJsonObject() {
            return new JsonObject()
                    .put("name", functionName)
                    .put("argument", functionArgument.toString());
        }
    }

    public AssistantMessage toAssistantMessage() {
        JsonObject x = new JsonObject();
        x.put("role", role.name());
        if (content != null) x.put("content", content.toString());
        if (!toolCallMap.isEmpty()) {
            JsonArray tool_calls = new JsonArray();
            toolCallMap.forEach((k, v) -> {
                tool_calls.add(v.toJsonObject());
            });
            x.put("tool_calls", tool_calls);
        }
        return new AssistantMessage(x);
    }
}
