package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.chunk;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.response.UsageImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatMessageResponseInChunkImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.ChatMessageResponseInChunk {
    public ChatMessageResponseInChunkImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public OutputChunkForMessageResponse getOutput() {
        return OutputChunkForMessageResponse.wrap(readJsonObject("output"));
    }

    @Nullable
    @Override
    public Usage getUsage() {
        JsonObject usage = readJsonObject("usage");
        if (usage == null) return null;
        return new UsageImpl(usage);
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
