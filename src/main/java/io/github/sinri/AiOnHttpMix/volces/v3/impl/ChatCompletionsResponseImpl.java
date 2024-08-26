package io.github.sinri.AiOnHttpMix.volces.v3.impl;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChatCompletionsResponseImpl extends UnmodifiableJsonifiableEntityImpl implements VolcesKit.ChatCompletionsResponse {

    public ChatCompletionsResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class ChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements Choice {

        public ChoiceImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class MessageImpl extends UnmodifiableJsonifiableEntityImpl implements Message {

        public MessageImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class ToolCallImpl extends UnmodifiableJsonifiableEntityImpl implements ToolCall {

        public ToolCallImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
