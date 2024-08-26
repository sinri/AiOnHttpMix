package io.github.sinri.AiOnHttpMix.moonshot.kimi.impl;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.KimiKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ChatCompletionsResponseImpl extends UnmodifiableJsonifiableEntityImpl implements KimiKit.ChatCompletionsResponse {
    public ChatCompletionsResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static class ChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements KimiKit.ChatCompletionsResponse.Choice {
        public ChoiceImpl(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }
    }
}
