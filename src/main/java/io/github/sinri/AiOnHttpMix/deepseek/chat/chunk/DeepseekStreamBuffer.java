package io.github.sinri.AiOnHttpMix.deepseek.chat.chunk;

import io.github.sinri.AiOnHttpMix.deepseek.chat.DeepseekChatResponse;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponse;
import io.github.sinri.AiOnHttpMix.utils.LLMStreamBuffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DeepseekStreamBuffer implements LLMStreamBuffer {
    private final JsonObject buffer;
    private final DeepseekChoiceBuffer choiceBuffer;
    private boolean metDoneFlag = false;

    public DeepseekStreamBuffer() {
        buffer = new JsonObject();
        choiceBuffer = new DeepseekChoiceBuffer();
    }

    public DeepseekStreamBuffer meetDoneFlag() {
        this.metDoneFlag = true;
        return this;
    }

    public boolean isMetDoneFlag() {
        return metDoneFlag;
    }

    public void accept(DeepseekResponseChunk chunk) {
        if (buffer.isEmpty()) {
            buffer.put("id", chunk.getId());
            buffer.put("created", chunk.getCreated());
            buffer.put("model", chunk.getModel());
        }

        List<DeepseekResponseChunk.ChoiceChunk> array = chunk.getChoices();
        if (array != null && !array.isEmpty()) {
            DeepseekResponseChunk.ChoiceChunk choiceChunk = array.get(0);
            choiceBuffer.accept(choiceChunk);
        }
    }

    public DeepseekChatResponse toResponse() {
        buffer.put("choices", new JsonArray().add(choiceBuffer.toChoice().cloneAsJsonObject()));
        return DeepseekChatResponse.wrap(buffer);
    }

    @Override
    public AnyLLMResponse toAnyLLMResponse() {
        return AnyLLMResponse.from(toResponse());
    }

    public static class DeepseekChoiceBuffer {
        private final StringBuilder contentBuffer = new StringBuilder();
        private final StringBuilder reasoningContentBuffer = new StringBuilder();
        private final JsonObject buffer;
        private String role;
        private final Map<Integer, ToolCallFunctionBuffer> toolCallMap = new TreeMap<>();

        public DeepseekChoiceBuffer() {
            buffer = new JsonObject();
        }

        public void accept(DeepseekResponseChunk.ChoiceChunk choiceChunk) {
            if (buffer.isEmpty()) {
                Integer index = choiceChunk.getIndex();
                buffer.put("index", index);
            }
            String finishReason = choiceChunk.getFinishReason();
            if (finishReason != null) {
                buffer.put("finish_reason", finishReason);
            }
            DeepseekResponseChunk.ChoiceChunkDelta delta = choiceChunk.getDelta();
            if (delta != null) {
                if (this.role == null) {
                    this.role = delta.getRole();
                }
                String reasoningContent = delta.getReasoningContent();
                if (reasoningContent != null) {
                    reasoningContentBuffer.append(reasoningContent);
                }
                String content = delta.getContent();
                if (content != null) {
                    contentBuffer.append(content);
                }

                List<DeepseekResponseChunk.ChoiceChunkDeltaToolCall> toolCalls = delta.getToolCalls();
                if (toolCalls != null && !toolCalls.isEmpty()) {
                    toolCalls.forEach(deltaToolCall -> {
                        toolCallMap.computeIfAbsent(deltaToolCall.getIndex(), ToolCallFunctionBuffer::new)
                                .accept(deltaToolCall);
                    });
                }
            }
        }

        public DeepseekChatResponse.Choice toChoice() {
            var message = new JsonObject()
                    .put("role", role)
                    .put("content", contentBuffer.toString())
                    .put("reasoning_content", reasoningContentBuffer.toString());


            if (!toolCallMap.isEmpty()) {
                JsonArray array = new JsonArray();
                List<Integer> keys = toolCallMap.keySet().stream().sorted().toList();
                for (Integer key : keys) {
                    ToolCallFunctionBuffer toolCallFunctionBuffer = toolCallMap.get(key);
                    array.add(toolCallFunctionBuffer.toJsonObject());
                }
                message.put("tool_calls", array);
            }

            buffer.put("message", message);

            return DeepseekChatResponse.Choice.wrap(buffer);
        }
    }

    public static class ToolCallFunctionBuffer {
        private final Integer index;
        private final StringBuilder functionName = new StringBuilder();
        private final StringBuilder functionArguments = new StringBuilder();

        public ToolCallFunctionBuffer(Integer index) {
            this.index = index;
        }

        public void accept(DeepseekResponseChunk.ChoiceChunkDeltaToolCall choiceChunkDeltaToolCall) {
            DeepseekResponseChunk.ChoiceChunkDeltaToolCallFunction function = choiceChunkDeltaToolCall.getFunction();
            if (function != null) {
                String name = function.getName();
                if (name != null) {
                    functionName.append(name);
                }
                String arguments = function.getArguments();
                if (arguments != null) {
                    functionArguments.append(arguments);
                }
            }
        }

        public JsonObject toJsonObject() {
            return new JsonObject()
                    .put("index", index)
                    .put("function", new JsonObject()
                            .put("name", functionName)
                            .put("arguments", functionArguments)
                    );
        }
    }
}
