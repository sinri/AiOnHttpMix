package io.github.sinri.AiOnHttpMix.deepseek.chat.chunk;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeepseekResponseChunk extends UnmodifiableJsonifiableEntityImpl {
    public DeepseekResponseChunk(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public Long getCreated() {
        return readLong("created");
    }

    public String getId() {
        return readString("id");
    }

    public String getModel() {
        return readString("model");
    }

    public String getObject() {
        return readString("object");
    }

    public Object getUsage() {
        return readValue("usage");
    }

    public List<ChoiceChunk> getChoices() {
        List<JsonObject> choices = readJsonObjectArray("choices");
        if (choices == null) {
            return null;
        }
        return choices.stream().map(ChoiceChunk::new).toList();
    }

    public static class ChoiceChunk extends UnmodifiableJsonifiableEntityImpl {
        public ChoiceChunk(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }

        public Integer getIndex() {
            return readInteger("index");
        }

        @Nullable
        public ChoiceChunkDelta getDelta() {
            JsonObject delta = readJsonObject("delta");
            if (delta == null) {
                return null;
            }
            return new ChoiceChunkDelta(delta);
        }

        @Nullable
        public String getFinishReason() {
            return readString("finish_reason");
        }
    }

    public static class ChoiceChunkDelta extends UnmodifiableJsonifiableEntityImpl {
        public ChoiceChunkDelta(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }

        public String getContent() {
            return readString("content");
        }

        public String getReasoningContent() {
            return readString("reasoning_content");
        }

        public String getRole() {
            return readString("role");
        }

        @Nullable
        public List<ChoiceChunkDeltaToolCall> getToolCalls() {
            List<JsonObject> toolCalls = readJsonObjectArray("tool_calls");
            if (toolCalls == null) {
                return null;
            }
            return toolCalls.stream().map(ChoiceChunkDeltaToolCall::new).toList();
        }
    }

    public static class ChoiceChunkDeltaToolCall extends UnmodifiableJsonifiableEntityImpl {

        public ChoiceChunkDeltaToolCall(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }

        public Integer getIndex() {
            return readInteger("index");
        }

        @Nullable
        public ChoiceChunkDeltaToolCallFunction getFunction() {
            JsonObject function = readJsonObject("function");
            if (function == null) {
                return null;
            }
            return new ChoiceChunkDeltaToolCallFunction(function);
        }
    }

    public static class ChoiceChunkDeltaToolCallFunction extends UnmodifiableJsonifiableEntityImpl {

        public ChoiceChunkDeltaToolCallFunction(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }

        @Nullable
        public String getName() {
            return readString("name");
        }

        public String getArguments() {
            return readString("arguments");
        }
    }
}
