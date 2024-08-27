package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.AiOnHttpMix.volces.v3.mixin.chunk.VolcesChatCompletionsResponseChunkMixin;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TempVolcesChatCompletionsResponse {
    private String id;
    private String model;
    private Integer created;
    private String object;
    private VolcesKit.Usage usage;
    private final Map<Integer, TempChoice> choiceMap;

    public TempVolcesChatCompletionsResponse() {
        choiceMap = new TreeMap<>();
    }

    public void accept(VolcesKit.ChatCompletionsResponseChunk chunk) {
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
        VolcesKit.Usage usage = chunk.getUsage();
        if (usage != null) {
            this.usage = usage;
        }

        List<VolcesChatCompletionsResponseChunkMixin.StreamChoice> choices = chunk.getChoices();
        if (choices != null && !choices.isEmpty()) {
            choices.forEach(choice -> {
                Integer index = choice.getIndex();
                choiceMap.computeIfAbsent(index, x -> new TempChoice()).accept(choice);
            });
        }
    }

    public VolcesKit.ChatCompletionsResponse toChatCompletionsResponse() {
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
        return VolcesKit.ChatCompletionsResponse.wrap(jsonObject);
    }

    public static class TempChoice {
        private Integer index;
        private String finishReason;
        private final TempMessage tempMessage;

        public TempChoice() {
            tempMessage = new TempMessage();
        }

        public VolcesKit.ChatCompletionsResponse.Choice toChoice() {
            return VolcesKit.ChatCompletionsResponse.Choice.wrap(new JsonObject()
                    .put("index", index)
                    .put("finish_reason", finishReason)
                    .put("message", tempMessage.toMessage().cloneAsJsonObject())
            );
        }

        public void accept(VolcesChatCompletionsResponseChunkMixin.StreamChoice streamChoice) {
            index = streamChoice.getIndex();
            finishReason = streamChoice.getFinishReason();

            VolcesChatCompletionsResponseChunkMixin.ChoiceDelta delta = streamChoice.getDelta();
            if (delta != null) {
                tempMessage.accept(delta);
            }
        }
    }

    public static class TempMessage {
        private VolcesKit.ChatRole role;
        private final StringBuilder content;
        private final Map<Integer, TempToolCall> toolCallMap;

        public TempMessage() {
            content = new StringBuilder();
            toolCallMap = new TreeMap<>();
        }

        public VolcesKit.ChatCompletionsResponse.Message toMessage() {
            JsonObject x = new JsonObject().put("role", role.name());
            if (content != null) {
                x.put("content", content.toString());
            }
            if (toolCallMap != null) {
                JsonArray toolCalls = new JsonArray();
                toolCallMap.forEach((index, tc) -> {
                    VolcesKit.ChatCompletionsResponse.ToolCall toolCall = tc.toToolCall();
                    toolCalls.add(toolCall.cloneAsJsonObject());
                });
                x.put("tool_calls", toolCalls);
            }
            return VolcesKit.ChatCompletionsResponse.Message.wrap(x);
        }

        public void accept(VolcesChatCompletionsResponseChunkMixin.ChoiceDelta delta) {
            var roleChunk = delta.getRole();
            if (roleChunk != null) {
                this.role = roleChunk;
            }

            var contentChunk = delta.getContent();
            if (contentChunk != null) {
                this.content.append(contentChunk);
            }

            List<VolcesChatCompletionsResponseChunkMixin.ChoiceDeltaToolCall> toolCalls = delta.getToolCalls();
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

        public VolcesKit.ChatCompletionsResponse.ToolCall toToolCall() {
            return VolcesKit.ChatCompletionsResponse.ToolCall.wrap(new JsonObject()
                    .put("id", id)
                    .put("type", type)
                    .put("index", index)
                    .put("function", new JsonObject()
                            .put("name", name.toString())
                            .put("arguments", arguments.toString())
                    )
            );
        }

        public void accept(VolcesChatCompletionsResponseChunkMixin.ChoiceDeltaToolCall deltaToolCall) {
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
            VolcesChatCompletionsResponseChunkMixin.FunctionCallChunk function = deltaToolCall.getFunction();
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
