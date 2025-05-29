package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.stream;

import io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.DoubaoMessageInResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponse;
import io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync.DoubaoResponseChoice;
import io.github.sinri.AiOnHttpMix.utils.StreamPieceCollector;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.ToolCallStreamPieceCollector;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DoubaoResponseBuffer implements StreamPieceCollector<DoubaoResponseChunk, DoubaoResponse> {
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

    @Override
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

    private static class DoubaoResponseChoiceBuffer implements StreamPieceCollector<DoubaoResponseChunkChoice, DoubaoResponseChoice> {
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

        @Override
        public DoubaoResponseChoice build() {
            JsonObject j = new JsonObject();
            j.put("index", index);
            j.put("finish_reason", finishReason);
            j.put("message", messageBuffer.build().toJsonObject());

            return DoubaoResponseChoice.wrap(j);
        }

    }

    private static class DoubaoMessageInResponseBuffer implements StreamPieceCollector<DoubaoResponseChunkChoiceDelta, DoubaoMessageInResponse> {
        private final StringBuilder reasoningContentBuffer = new StringBuilder();
        private final StringBuilder contentBuffer = new StringBuilder();
        private final Map<Integer, ToolCallStreamPieceCollector> toolCallBufferMap;
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
                    toolCallBufferMap.computeIfAbsent(i, x -> new ToolCallStreamPieceCollector())
                                     .accept(toolCalls.get(i));
                }
            }
        }

        @Override
        public DoubaoMessageInResponse build() {
            JsonObject j = new JsonObject();
            j.put("role", role);
            j.put("reasoning_content", reasoningContentBuffer.toString());
            j.put("content", contentBuffer.toString());

            if (!toolCallBufferMap.isEmpty()) {
                JsonArray a = new JsonArray();
                for (int i = 0; i < toolCallBufferMap.size(); i++) {
                    ToolCallStreamPieceCollector doubaoToolCallBuffer = toolCallBufferMap.get(i);
                    ToolCall doubaoToolCall = doubaoToolCallBuffer.build();
                    a.add(doubaoToolCall.toJsonObject());
                }
                j.put("tool_calls", a);
            }

            return DoubaoMessageInResponse.wrap(j);
        }
    }

}
