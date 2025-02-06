package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.QwenResponseBase;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.response.QwenResponseInMessageFormat;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolCall;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMResponse;
import io.github.sinri.AiOnHttpMix.utils.LLMStreamBuffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class QwenStreamBuffer implements LLMStreamBuffer {
    private final TempChoice tempChoice;
    private QwenResponseBase.Usage usage;

    public QwenStreamBuffer() {
        this.tempChoice = new TempChoice();
    }

    public void acceptChunkData(QwenResponseChunk chatMessageResponseInChunk) {
        AigcMix.getVerboseLogger().debug(x -> x
                .message("io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenStreamBuffer.acceptChunkData::chatMessageResponseInChunk")
                .context(chatMessageResponseInChunk.cloneAsJsonObject()));
        usage = chatMessageResponseInChunk.getUsage();
        QwenResponseChunk.OutputChunkForMessageResponse output = chatMessageResponseInChunk.getOutput();
        if (output != null) {
            List<QwenResponseChunk.OutputChunkForMessageResponse.Choice> choices = output.getChoices();
            if (choices != null && !choices.isEmpty()) {
                QwenResponseChunk.OutputChunkForMessageResponse.Choice choice = choices.get(0);
                AigcMix.getVerboseLogger().debug(x -> x
                        .message("io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk.QwenStreamBuffer.acceptChunkData::choice")
                        .context(choice.cloneAsJsonObject()));
                tempChoice.acceptChoice(choice);
            }
        }
    }

    public QwenResponseInMessageFormat toChatMessageResponse() {
        var j = new JsonObject();
        if (usage != null) {
            j.put("usage", usage.cloneAsJsonObject());
        }
        j.put("output", new JsonObject()
                .put("choices", new JsonArray()
                        .add(tempChoice.toJsonObject())
                )
        );
        return QwenResponseInMessageFormat.wrap(200, j);
    }

    /**
     * @return
     * @since 1.1.5
     */
    @Override
    public AnyLLMResponse toAnyLLMResponse() {
        return AnyLLMResponse.from(this.toChatMessageResponse());
    }

    public static class TempChoice {
        private final TempMessage tempMessage;
        private String finishReason;

        public TempChoice() {
            this.tempMessage = new TempMessage();
        }

        public void acceptChoice(QwenResponseChunk.OutputChunkForMessageResponse.Choice choice) {
            this.finishReason = choice.getFinishReason();

            QwenMessage message = choice.getMessage();
            this.tempMessage.acceptMessage(message);
        }

        public JsonObject toJsonObject() {
            return new JsonObject()
                    .put("finish_reason", finishReason)
                    .put("message", tempMessage.toJsonObject());
        }
    }

    public static class TempMessage {
        private final StringBuilder content;
        private final TempToolCalls toolCalls;
        private QwenRole role;

        public TempMessage() {
            content = new StringBuilder();
            toolCalls = new TempToolCalls();
        }

        public void acceptMessage(QwenMessage message) {
            if (message.getRole() != null) {
                this.role = message.getRole();
            }
            if (message.getContent() != null) {
                this.content.append(message.getContent());
            }
            if (message.getToolCalls() != null) {
                List<QwenToolCall> toolCallList = message.getToolCalls();
                toolCallList.forEach(toolCalls::accept);
            }
        }

        public JsonObject toJsonObject() {
            JsonObject entries = new JsonObject();
            if (this.role != null) {
                entries.put("role", this.role.name());
            }
            entries.put("content", this.content.toString());
            List<QwenToolCall.FunctionCall> tcList = this.toolCalls.toToolCalls();
            if (!tcList.isEmpty()) {
                JsonArray tcArray = new JsonArray();
                tcList.forEach(tc -> {
                    tcArray.add(new JsonObject()
                            .put("function", tc.cloneAsJsonObject())
                            .put("type", "function")
                    );
                });
                entries.put("tool_calls", tcArray);
            }

            return entries;
        }
    }

    public static class TempToolCalls {
        Map<Integer, TempFunctionCall> map = new TreeMap<>();

        public TempToolCalls() {

        }

        public void accept(QwenToolCall toolCall) {
            Integer index = toolCall.getIndex();
            String type = toolCall.getType();
            QwenToolCall.FunctionCall function = toolCall.getFunction();
            if ("function".equals(type)) {
                map.computeIfAbsent(index, x -> new TempFunctionCall()).accept(function);
            }
        }

        public List<QwenToolCall.FunctionCall> toToolCalls() {
            return map.values().stream().map(TempFunctionCall::toFunctionCall).toList();
        }
    }

    public static class TempFunctionCall {
        private final StringBuilder arguments;
        private String name;

        public TempFunctionCall() {
            arguments = new StringBuilder();
        }

        public void accept(QwenToolCall.FunctionCall fragment) {
            String fragmentName = fragment.getName();
            if (fragmentName != null && !fragmentName.isEmpty()) {
                this.name = fragmentName;
            }
            String fragmentArguments = fragment.getArguments();
            if (fragmentArguments != null && !fragmentArguments.isEmpty()) {
                this.arguments.append(fragmentArguments);
            }
        }

        public QwenToolCall.FunctionCall toFunctionCall() {
            return QwenToolCall.FunctionCall.wrap(new JsonObject()
                    .put("name", name)
                    .put("arguments", arguments.toString())
            );
        }
    }
}
