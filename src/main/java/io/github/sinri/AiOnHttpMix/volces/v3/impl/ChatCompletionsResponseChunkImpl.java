package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChatCompletionsResponseChunkImpl extends UnmodifiableJsonifiableEntityImpl implements VolcesKit.ChatCompletionsResponseChunk {

    public ChatCompletionsResponseChunkImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class StreamChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements StreamChoice {

        public StreamChoiceImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static  class ChoiceDeltaImpl extends UnmodifiableJsonifiableEntityImpl implements ChoiceDelta {
        public ChoiceDeltaImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class ChoiceDeltaToolCallImpl extends UnmodifiableJsonifiableEntityImpl implements ChoiceDeltaToolCall {

        public ChoiceDeltaToolCallImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class FunctionCallChunkImpl extends UnmodifiableJsonifiableEntityImpl implements FunctionCallChunk {

        public FunctionCallChunkImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
