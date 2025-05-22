package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.stream;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.QwenMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.vision.Content;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponse;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponseOutput;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.response.sync.QwenResponseOutputChoice;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.tool.QwenFunctionToolCall;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.tool.QwenToolCall;
import io.github.sinri.AiOnHttpMix.utils.StreamPieceCollector;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class QwenResponseBuffer {
    private final UsageBuffer usageBuffer;
    private final OutputBuffer outputBuffer;
    private String requestId;

    public QwenResponseBuffer() {
        usageBuffer = new UsageBuffer();
        outputBuffer = new OutputBuffer();
    }

    public void accept(QwenResponseChunk chunk) {
        if (requestId == null) {
            requestId = chunk.getRequestId();
        }
        usageBuffer.accept(chunk.getUsage());

        QwenResponseOutput output = chunk.getOutput();
        outputBuffer.accept(output);
    }

    public QwenResponse toQwenResponse() {

        return QwenResponse.wrap(new JsonObject()
                .put("request_id", requestId)
                .put("usage", usageBuffer.toUsage())
                .put("output", outputBuffer.toOutput().cloneAsJsonObject())
        );
    }

    public static class OutputBuffer {
        private final Map<Integer, ChoiceBuffer> choiceBufferMap = new HashMap<>();

        public OutputBuffer() {

        }

        public void accept(QwenResponseOutput output) {
            List<QwenResponseOutputChoice> choices = output.getChoices();
            for (int i = 0; i < choices.size(); i++) {
                var choice = choices.get(i);
                choiceBufferMap.computeIfAbsent(i, x -> new ChoiceBuffer())
                               .accept(choice);
            }
        }

        public QwenResponseOutput toOutput() {
            JsonArray choices = new JsonArray();
            for (int i = 0; i < choiceBufferMap.size(); i++) {
                ChoiceBuffer choiceBuffer = choiceBufferMap.get(i);
                QwenResponseOutputChoice choice = choiceBuffer.toChoice();
                choices.add(choice.cloneAsJsonObject());
            }
            return QwenResponseOutput.wrap(new JsonObject()
                    .put("choices", choices));
        }
    }

    public static class ChoiceBuffer {
        private final MessageBuffer messageBuffer;
        private String finishReason;

        public ChoiceBuffer() {
            messageBuffer = new MessageBuffer();
        }

        public void accept(QwenResponseOutputChoice choice) {
            finishReason = choice.getFinishReason();

            QwenMessageInResponse message = choice.getMessage();
            messageBuffer.accept(message);
        }

        public QwenResponseOutputChoice toChoice() {
            return QwenResponseOutputChoice.wrap(new JsonObject()
                    .put("finish_reason", finishReason)
                    .put("message", messageBuffer.toMessage().cloneAsJsonObject())
            );
        }
    }

    public static class MessageBuffer {
        private final Map<Integer, ContentOfTextBuffer> contentBufferMap;
        private final StringBuilder content;
        private final StringBuilder reasoningContent;
        private final Map<Integer, ToolCallBuffer> toolCallBufferMap;
        private String role;

        public MessageBuffer() {
            content = new StringBuilder();
            contentBufferMap = new HashMap<>();
            reasoningContent = new StringBuilder();
            toolCallBufferMap = new HashMap<>();
        }

        public void accept(QwenMessageInResponse message) {
            role = message.getRole();

            String contentPiece = message.getContent();
            if (contentPiece != null) {
                content.append(contentPiece);
            }

            List<String> contents = message.getContents();
            for (int i = 0; i < contents.size(); i++) {
                String contentTextPiece = contents.get(i);
                contentBufferMap.computeIfAbsent(i, x -> new ContentOfTextBuffer())
                                .accept(contentTextPiece);
            }

            String reasoningContentPiece = message.getReasoningContent();
            if (reasoningContentPiece != null) {
                reasoningContent.append(reasoningContentPiece);
            }

            List<ToolCall> toolCalls = message.getToolCalls();
            for (int i = 0; i < toolCalls.size(); i++) {
                ToolCall toolCall = toolCalls.get(i);
                toolCallBufferMap.computeIfAbsent(i, x -> new ToolCallBuffer())
                                 .accept(toolCall);
            }
        }

        public QwenMessageInResponse toMessage() {
            JsonObject x = new JsonObject()
                    .put("role", role);
            if (contentBufferMap.isEmpty()) {
                x.put("content", content.toString());
            } else {
                JsonArray array = new JsonArray();
                for (int i = 0; i < contentBufferMap.size(); i++) {
                    var cb = contentBufferMap.get(i);
                    Content built = cb.build();
                    array.add(built.toJsonObject());
                }
                x.put("content", array);
            }
            if (!reasoningContent.isEmpty()) {
                x.put("reasoning_content", reasoningContent.toString());
            }
            if (!toolCallBufferMap.isEmpty()) {
                JsonArray toolCallArray = new JsonArray();
                for (int i = 0; i < toolCallBufferMap.size(); i++) {
                    ToolCallBuffer toolCallBuffer = toolCallBufferMap.get(i);
                    toolCallArray.add(toolCallBuffer.toToolCall().cloneAsJsonObject());
                }
                x.put("tool_calls", toolCallArray);
            }
            return QwenMessageInResponse.wrap(x);
        }
    }

    /**
     * For Vision Model, i.e. QwenVL series.
     */
    public static class ContentOfTextBuffer implements StreamPieceCollector<String, Content> {
        private final StringBuilder textBuilder = new StringBuilder();

        @Override
        public void accept(String piece) {
            textBuilder.append(piece);
        }

        @Override
        public Content build() {
            return Content.wrap(new JsonObject()
                    .put("text", textBuilder.toString()));
        }
    }

    public static class ToolCallBuffer {
        private final FunctionToolCallBuffer functionToolCallBuffer;
        private String toolCallId;
        private Integer index;
        private String type;

        public ToolCallBuffer() {
            functionToolCallBuffer = new FunctionToolCallBuffer();
        }

        public void accept(ToolCall toolCall) {
            String id = toolCall.getId();
            if (!Keel.stringHelper().isNullOrBlank(id)) {
                toolCallId = id;
            }
            index = toolCall.getIndex();
            type = toolCall.getType();
            FunctionToolCall function = toolCall.getFunction();
            functionToolCallBuffer.accept(function);
        }

        public QwenToolCall toToolCall() {
            return new QwenToolCall(new JsonObject()
                    .put("id", toolCallId)
                    .put("index", index)
                    .put("type", type)
                    .put("function", functionToolCallBuffer.toFunctionToolCall().cloneAsJsonObject())
            );
        }
    }

    public static class FunctionToolCallBuffer {
        private final StringBuilder nameBuffer = new StringBuilder();
        private final StringBuilder argumentsBuffer = new StringBuilder();

        public FunctionToolCallBuffer() {

        }

        public void accept(FunctionToolCall functionToolCall) {
            String name = functionToolCall.getName();
            if (name != null) {
                nameBuffer.append(name);
            }
            String arguments = functionToolCall.getArguments();
            if (arguments != null) {
                argumentsBuffer.append(arguments);
            }
        }

        public QwenFunctionToolCall toFunctionToolCall() {
            return new QwenFunctionToolCall(new JsonObject()
                    .put("name", nameBuffer.toString())
                    .put("arguments", argumentsBuffer.toString())
            );
        }
    }

    public static class UsageBuffer {
        private int total_tokens = 0;
        private int input_tokens = 0;
        private int output_tokens = 0;

        public UsageBuffer() {

        }

        public void accept(JsonObject usage) {
            total_tokens += usage.getInteger("total_tokens");
            input_tokens += usage.getInteger("input_tokens");
            output_tokens += usage.getInteger("output_tokens");
        }

        public JsonObject toUsage() {
            return new JsonObject()
                    .put("total_tokens", total_tokens)
                    .put("input_tokens", input_tokens)
                    .put("output_tokens", output_tokens);
        }
    }
}
