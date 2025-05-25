package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponseChoice;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonFunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DoubaoResponseBuffer {
    private final Map<Integer, DoubaoResponseChoiceBuffer> choiceBufferMap;
    private String id;
    private String model;
    private String service_tier;
    private Integer created;
    private String object;

    public DoubaoResponseBuffer() {
        choiceBufferMap = new HashMap<>();
    }

    public void accept(DoubaoResponseChunk chunk) {
        if (id == null) {
            id = chunk.getId();
        }
        if (model == null) {
            model = chunk.getModel();
        }
        if (service_tier == null) {
            service_tier = chunk.getServiceTier();
        }
        if (created == null) {
            created = chunk.getCreated();
        }
        if (object == null) {
            object = chunk.getObject();
        }

        List<DoubaoResponseChunkChoice> choices = chunk.getChoices();
        for (int i = 0; i < choices.size(); i++) {
            choiceBufferMap.computeIfAbsent(i, x -> new DoubaoResponseChoiceBuffer())
                           .accept(choices.get(i));
        }
    }

    public DoubaoResponse build() {
        JsonObject j = new JsonObject();
        j.put("id", id);
        j.put("model", model);
        j.put("service_tier", service_tier);
        j.put("created", created);
        j.put("object", object);

        JsonArray choices = new JsonArray();
        for (int i = 0; i < choiceBufferMap.size(); i++) {
            DoubaoResponseChoiceBuffer doubaoResponseChoiceBuffer = choiceBufferMap.get(i);
            DoubaoResponseChoice choice = doubaoResponseChoiceBuffer.build();
            choices.add(choice.cloneAsJsonObject());
        }
        j.put("choices", choices);

        return DoubaoResponse.wrap(j);
    }

    private static class DoubaoResponseChoiceBuffer {
        private final DoubaoMessageInResponseBuffer messageBuffer;
        private Integer index;
        private String finishReason;

        public DoubaoResponseChoiceBuffer() {
            messageBuffer = new DoubaoMessageInResponseBuffer();
        }

        public void accept(DoubaoResponseChunkChoice chunkChoice) {
            if (index == null) {
                index = chunkChoice.getIndex();
            }
            if (finishReason == null) {
                finishReason = chunkChoice.getFinishReason();
            }

            DoubaoResponseChunkChoiceDelta delta = chunkChoice.getDelta();
            messageBuffer.accept(delta);
        }

        public DoubaoResponseChoice build() {
            JsonObject j = new JsonObject();
            j.put("index", index);
            j.put("finish_reason", finishReason);
            j.put("message", messageBuffer.build().toJsonObject());

            return DoubaoResponseChoice.wrap(j);
        }

    }

    private static class DoubaoMessageInResponseBuffer {
        private final StringBuilder reasoningContentBuffer = new StringBuilder();
        private final StringBuilder contentBuffer = new StringBuilder();
        private final Map<Integer, DoubaoToolCallBuffer> toolCallBufferMap;
        private String role;

        public DoubaoMessageInResponseBuffer() {
            toolCallBufferMap = new HashMap<>();
        }

        public void accept(DoubaoResponseChunkChoiceDelta delta) {
            if (role == null) {
                role = delta.getRole();
            }
            String reasoningContent = delta.getReasoningContent();
            if (!Keel.stringHelper().isNullOrBlank(reasoningContent)) {
                reasoningContentBuffer.append(reasoningContent);
            }
            String content = delta.getContent();
            if (!Keel.stringHelper().isNullOrBlank(content)) {
                contentBuffer.append(content);
            }
            List<CommonToolCall> toolCalls = delta.getToolCalls();
            if (toolCalls != null) {
                for (int i = 0; i < toolCalls.size(); i++) {
                    toolCallBufferMap.computeIfAbsent(i, x -> new DoubaoToolCallBuffer())
                                     .accept(toolCalls.get(i));
                }
            }
        }

        public DoubaoMessageInResponse build() {
            JsonObject j = new JsonObject();
            j.put("role", role);
            j.put("reasoning_content", reasoningContentBuffer.toString());
            j.put("content", contentBuffer.toString());

            if (!toolCallBufferMap.isEmpty()) {
                JsonArray a = new JsonArray();
                for (int i = 0; i < toolCallBufferMap.size(); i++) {
                    DoubaoToolCallBuffer doubaoToolCallBuffer = toolCallBufferMap.get(i);
                    CommonToolCall doubaoToolCall = doubaoToolCallBuffer.build();
                    a.add(doubaoToolCall.cloneAsJsonObject());
                }
                j.put("tool_calls", a);
            }

            return DoubaoMessageInResponse.wrap(j);
        }
    }

    private static class DoubaoToolCallBuffer {
        private final DoubaoFunctionToolCallBuffer functionToolCallBuffer = new DoubaoFunctionToolCallBuffer();
        private String toolCallId;
        private String type;
        private Integer index;

        public void accept(CommonToolCall doubaoToolCall) {
            if (toolCallId == null) {
                toolCallId = doubaoToolCall.getId();
            }
            if (type == null) {
                type = doubaoToolCall.getType();
            }
            if (index == null) {
                index = doubaoToolCall.getIndex();
            }
            if (Objects.equals("function", type)) {
                FunctionToolCall function = doubaoToolCall.getFunction();
                functionToolCallBuffer.accept(function);
            }
        }

        public CommonToolCall build() {
            JsonObject j = new JsonObject();
            j.put("index", index);
            j.put("id", toolCallId);
            j.put("type", type);
            if (Objects.equals("function", type)) {
                j.put("function", functionToolCallBuffer.build().toJsonObject());
            }
            return new CommonToolCall(j);
        }
    }

    private static class DoubaoFunctionToolCallBuffer {
        private final StringBuilder nameBuffer = new StringBuilder();
        private final StringBuilder argumentsBuffer = new StringBuilder();

        public void accept(FunctionToolCall functionToolCall) {
            String name = functionToolCall.getName();
            if (!Keel.stringHelper().isNullOrBlank(name)) {
                nameBuffer.append(name);
            }
            String arguments = functionToolCall.getArguments();
            if (!Keel.stringHelper().isNullOrBlank(arguments)) {
                argumentsBuffer.append(arguments);
            }
        }

        public FunctionToolCall build() {
            JsonObject j = new JsonObject();
            j.put("name", nameBuffer.toString());
            j.put("arguments", argumentsBuffer.toString());
            return new CommonFunctionToolCall(j);
        }
    }
}
