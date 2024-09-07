package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGptRole;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.message.AssistantMessage;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseChoice;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseFunctionCall;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response.OpenAIChatGptResponseToolCall;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Map;
import java.util.TreeMap;

public class OpenAIChatGptStreamBuffer {
    private String finishReason;
    private ChatGptRole role;
    private StringBuilder content;
    private final Map<Integer, TempToolCall> toolCallMap = new TreeMap<>();

    public OpenAIChatGptStreamBuffer() {
    }

    public void acceptFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public void acceptRole(ChatGptRole role) {
        this.role = role;
    }

    /**
     * @param toolCallInChunk First time met `tool_calls`.
     */
    public synchronized void acceptToolCall(OpenAIChatGptResponseToolCall toolCallInChunk) {
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
        private final TempFunctionCall function;

        public TempToolCall(OpenAIChatGptResponseToolCall toolCallInChunk) {
            this.type = toolCallInChunk.getType();
            this.id = toolCallInChunk.getId();
            this.index = toolCallInChunk.getIndex();
            OpenAIChatGptResponseFunctionCall f = toolCallInChunk.getFunction();
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
                    .put("arguments", functionArgument.toString());
        }
    }

    @Deprecated(since = "1.1.0")
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

    public OpenAIChatGptResponseChoice toResponseChoice() {
        JsonObject messageJsonObject = new JsonObject();
        if (role != null) messageJsonObject.put("role", role.name());
        if (content != null) messageJsonObject.put("content", content.toString());
        if (!toolCallMap.isEmpty()) {
            JsonArray tool_calls = new JsonArray();
            toolCallMap.forEach((k, v) -> {
                tool_calls.add(v.toJsonObject());
            });
            messageJsonObject.put("tool_calls", tool_calls);
        }

        return OpenAIChatGptResponseChoice.wrap(new JsonObject()
                .put("index", 0)
                .put("finish_reason", finishReason)
                .put("message", messageJsonObject)
        );
    }
}
