package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class OpenAIChatGptResponseChunkChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIChatGptResponseChunkChoice {
    public OpenAIChatGptResponseChunkChoiceImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public @Nullable OpenAIChatGptResponseChunkChoiceDelta getDelta() {
        JsonObject delta = readJsonObject("delta");
        if (delta == null) return null;
        return new OpenAIChatGptResponseChunkChoiceDeltaImpl(delta);
    }
}
