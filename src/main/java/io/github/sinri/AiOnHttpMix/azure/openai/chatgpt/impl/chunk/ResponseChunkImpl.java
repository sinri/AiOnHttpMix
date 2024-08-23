package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ResponseChunkImpl extends UnmodifiableJsonifiableEntityImpl implements ChatGPTKit.ResponseChunk {
    public ResponseChunkImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public @NotNull List<ChoiceInChunk> getChoices() {
        List<ChoiceInChunk> list = new ArrayList<>();

        List<JsonObject> choices = readJsonObjectArray("choices");
        if (choices != null) {
            choices.forEach(jsonObject -> {
                ChoiceInChunkImpl x = new ChoiceInChunkImpl(jsonObject);
                list.add(x);
            });
        }
        return list;
    }


}
