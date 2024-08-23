package io.github.sinri.AiOnHttpMix.dashscope.qwen.impl.response;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ChatMessageResponseImpl extends UnmodifiableJsonifiableEntityImpl implements QwenKit.ChatMessageResponse {
    public ChatMessageResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public OutputForMessageResponse getOutput() {
        JsonObject output = readJsonObject("output");
        Objects.requireNonNull(output);
        return new OutputForMessageResponseImpl(output);
    }

    @Override
    public Usage getUsage() {
        JsonObject usage = readJsonObject("usage");
        Objects.requireNonNull(usage);
        return new UsageImpl(usage);
    }

    public static class OutputForMessageResponseImpl extends UnmodifiableJsonifiableEntityImpl implements OutputForMessageResponse {
        public OutputForMessageResponseImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }

        @Override
        public @Nullable List<Choice> getChoices() {
            List<JsonObject> choices = readJsonObjectArray("choices");
            if (choices == null) return null;
            return choices.stream().map(Choice::wrap).toList();
        }

    }

    public static class ChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements OutputForMessageResponse.Choice {
        public ChoiceImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

}
