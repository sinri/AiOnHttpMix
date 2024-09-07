package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface OpenAIChatGptResponseFunctionCall extends UnmodifiableJsonifiableEntity {
    static OpenAIChatGptResponseFunctionCall wrap(JsonObject jsonObject) {
        return new OpenAIChatGptResponseFunctionCallImpl(jsonObject);
    }

    @Nullable
    default String getName() {
        return readString("name");
    }

    /**
     * @since 1.0.4 Fix the name
     */
    @Nullable
    default String getArguments() {
        return readString("arguments");
    }
}
