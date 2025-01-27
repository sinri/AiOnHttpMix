package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class DeepseekToolCallInResponseImpl extends UnmodifiableJsonifiableEntityImpl implements DeepseekMessageInResponse.DeepseekToolCallInResponse {
    public DeepseekToolCallInResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class FunctionCallImpl extends UnmodifiableJsonifiableEntityImpl implements DeepseekMessageInResponse.DeepseekToolCallInResponse.FunctionCall {

        public FunctionCallImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
