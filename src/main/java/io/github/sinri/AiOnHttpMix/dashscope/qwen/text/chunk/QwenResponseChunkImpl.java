package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.chunk;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class QwenResponseChunkImpl extends UnmodifiableJsonifiableEntityImpl implements QwenResponseChunk {
    private final int statusCode;

    public QwenResponseChunkImpl(int statusCode, @NotNull JsonObject jsonObject) {
        super(jsonObject);
        this.statusCode = statusCode;
    }

    @Override
    public OutputChunkForMessageResponse getOutput() {
        return OutputChunkForMessageResponse.wrap(readJsonObject("output"));
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Nullable
    @Override
    public Usage getUsage() {
        JsonObject usage = readJsonObject("usage");
        if (usage == null) return null;
        return Usage.wrap(usage);
    }

    public static class OutputChunkForMessageResponseImpl extends UnmodifiableJsonifiableEntityImpl implements OutputChunkForMessageResponse {

        public OutputChunkForMessageResponseImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class ChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements OutputChunkForMessageResponse.Choice {

        public ChoiceImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }


    }

//    @Deprecated(forRemoval = true)
//    public static class MessageImpl extends UnmodifiableJsonifiableEntityImpl implements OutputChunkForMessageResponse.Choice.Message {
//        public MessageImpl(@NotNull JsonObject jsonObject) {
//            super(jsonObject);
//        }
//    }
}
