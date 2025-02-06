package io.github.sinri.AiOnHttpMix.deepseek.chat;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class DeepseekChatResponseImpl extends UnmodifiableJsonifiableEntityImpl implements DeepseekChatResponse {

    public DeepseekChatResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class ChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements DeepseekChatResponse.Choice {
        public ChoiceImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class ToolCallImpl extends UnmodifiableJsonifiableEntityImpl implements DeepseekChatResponse.ToolCall {

        public ToolCallImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }

    public static class ToolCallFunctionImpl extends UnmodifiableJsonifiableEntityImpl implements DeepseekChatResponse.ToolCallFunction {
        public ToolCallFunctionImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
