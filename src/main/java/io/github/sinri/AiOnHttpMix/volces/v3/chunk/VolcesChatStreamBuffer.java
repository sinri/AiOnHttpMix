package io.github.sinri.AiOnHttpMix.volces.v3.chunk;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.AiOnHttpMix.volces.v3.response.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VolcesChatStreamBuffer {
    private String id;
    private String model;
    private Integer created;
    private String object;
    private VolcesChatUsage usage;
    private final Map<Integer, TempChoice> choiceMap;

    public VolcesChatStreamBuffer() {
        choiceMap = new TreeMap<>();
    }

    public void accept(VolcesChatResponseChunk chunk) {
        String id = chunk.getId();
        if (id != null) {
            this.id = id;
        }
        String model = chunk.getModel();
        if (model != null) {
            this.model = model;
        }
        Integer created = chunk.getCreated();
        if (created != null) {
            this.created = created;
        }
        String object = chunk.getObject();
        if (object != null) {
            this.object = object;
        }
        VolcesChatUsage usage = chunk.getUsage();
        if (usage != null) {
            this.usage = usage;
        }

        List<VolcesChatResponseChunk.StreamChoice> choices = chunk.getChoices();
        if (choices != null && !choices.isEmpty()) {
            choices.forEach(choice -> {
                Integer index = choice.getIndex();
                choiceMap.computeIfAbsent(index, x -> new TempChoice()).accept(choice);
            });
        }
    }

    public VolcesChatResponse toChatCompletionsResponse() {
        JsonArray jsonArray = new JsonArray();
        choiceMap.forEach((index, choice) -> {
            jsonArray.add(choice.toChoice().cloneAsJsonObject());
        });
        JsonObject jsonObject = new JsonObject()
                .put("id", id)
                .put("model", model)
                .put("created", created)
                .put("object", object)
                .put("choices", jsonArray);
        if (usage != null) {
            jsonObject.put("usage", usage.cloneAsJsonObject());
        }
        return VolcesChatResponse.wrap(jsonObject);
    }

    public static class TempChoice {
        private Integer index;
        private String finishReason;
        private final TempMessage tempMessage;

        public TempChoice() {
            tempMessage = new TempMessage();
        }

        public VolcesChatResponseChoice toChoice() {
            return VolcesChatResponseChoice.wrap(new JsonObject()
                    .put("index", index)
                    .put("finish_reason", finishReason)
                    .put("message", tempMessage.toMessage().cloneAsJsonObject())
            );
        }

        public void accept(VolcesChatResponseChunk.StreamChoice streamChoice) {
            index = streamChoice.getIndex();
            finishReason = streamChoice.getFinishReason();

            VolcesChatResponseChunk.ChoiceDelta delta = streamChoice.getDelta();
            if (delta != null) {
                tempMessage.accept(delta);
            }
        }
    }

    public static class TempMessage {
        private VolcesChatRole role;
        private @Nullable StringBuilder content;
        private final Map<Integer, TempToolCall> toolCallMap;

        public TempMessage() {
            toolCallMap = new TreeMap<>();
        }

        public VolcesChatResponseMessage toMessage() {
            JsonObject x = new JsonObject();
            if (role != null) {
                x.put("role", role.name());
            }
            if (content != null) {
                x.put("content", content.toString());
            }
            if (!toolCallMap.isEmpty()) {
                JsonArray toolCalls = new JsonArray();
                toolCallMap.forEach((index, tc) -> {
                    VolcesChatMessageToolCallForResponse toolCall = tc.toToolCall();
                    toolCalls.add(toolCall.cloneAsJsonObject());
                });
                x.put("tool_calls", toolCalls);
            }
            return VolcesChatResponseMessage.wrap(x);
        }

        public void accept(VolcesChatResponseChunk.ChoiceDelta delta) {
            var roleChunk = delta.getRole();
            if (roleChunk != null) {
                this.role = roleChunk;
            }

            var contentChunk = delta.getContent();
            if (contentChunk != null) {
                if (this.content == null) {
                    this.content = new StringBuilder();
                }
                this.content.append(contentChunk);
            }

            List<VolcesChatResponseChunk.ChoiceDeltaToolCall> toolCalls = delta.getToolCalls();
            if (toolCalls != null && !toolCalls.isEmpty()) {
                toolCalls.forEach(toolCall -> {
                    Integer index = toolCall.getIndex();
                    toolCallMap.computeIfAbsent(index, x -> new TempToolCall())
                            .accept(toolCall);
                });
            }
        }
    }

    public static class TempToolCall {
        private String id;
        private String type;
        private Integer index;
        private final StringBuilder name;
        private final StringBuilder arguments;

        public TempToolCall() {
            name = new StringBuilder();
            arguments = new StringBuilder();
        }

        public VolcesChatMessageToolCallForResponse toToolCall() {
            return VolcesChatMessageToolCallForResponse.wrap(new JsonObject()
                    .put("id", id)
                    .put("type", type)
                    .put("index", index)
                    .put("function", new JsonObject()
                            .put("name", name.toString())
                            .put("arguments", arguments.toString())
                    )
            );
        }

        public void accept(VolcesChatResponseChunk.ChoiceDeltaToolCall deltaToolCall) {
            var id = deltaToolCall.getId();
            if (id != null) {
                this.id = id;
            }
            String type = deltaToolCall.getType();
            if (type != null) {
                this.type = type;
            }
            Integer index = deltaToolCall.getIndex();
            if (index != null) {
                this.index = index;
            }
            VolcesChatResponseChunk.FunctionCallChunk function = deltaToolCall.getFunction();
            String name = function.getName();
            if (name != null) {
                this.name.append(name);
            }
            String arguments = function.getArguments();
            if (arguments != null) {
                this.arguments.append(arguments);
            }
        }
    }
}
