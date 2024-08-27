package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.core.filter.OpenAIPromptFilterResults;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface OpenAIResponseChunk extends UnmodifiableJsonifiableEntity {
    static OpenAIResponseChunk wrap(JsonObject json) {
        return new OpenAIResponseChunkImpl(json);
    }

    default Integer getCreated() {
        return readInteger("created");
    }

    default String getId() {
        return readString("id");
    }

    default String getObject() {
        return readString("object");
    }

    @Nullable
    default String getModel() {
        return readString("model");
    }

    @Nullable
    default String getSystemFingerprint() {
        return readString("system_fingerprint");
    }

    /**
     * 一般存在于第一个Piece中。
     */
    @Nullable
    default List<OpenAIPromptFilterResults> getPromptFilterResults() {
        var array = readJsonObjectArray("prompt_filter_results");
        if (array == null) return null;
        List<OpenAIPromptFilterResults> list = new ArrayList<>();
        array.forEach(x -> {
            var y = OpenAIPromptFilterResults.wrap(x);
            list.add(y);
        });
        return list;
    }

    @NotNull
    List<OpenAIChatGptResponseChunkChoice> getChoices();


    // logprobs
}
