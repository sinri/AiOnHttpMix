package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class OpenAIResponseChunkImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIResponseChunk {
    public OpenAIResponseChunkImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public @NotNull List<OpenAIChatGptResponseChunkChoice> getChoices() {
        List<OpenAIChatGptResponseChunkChoice> list = new ArrayList<>();

        List<JsonObject> choices = readJsonObjectArray("choices");
        if (choices != null) {
            choices.forEach(jsonObject -> {
                OpenAIChatGptResponseChunkChoiceImpl x = new OpenAIChatGptResponseChunkChoiceImpl(jsonObject);
                list.add(x);
            });
        }
        return list;
    }


}
