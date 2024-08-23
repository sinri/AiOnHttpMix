package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.response;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class FunctionCallImpl extends UnmodifiableJsonifiableEntityImpl implements ChatGPTKit.ToolCall.FunctionCall {
    public FunctionCallImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
