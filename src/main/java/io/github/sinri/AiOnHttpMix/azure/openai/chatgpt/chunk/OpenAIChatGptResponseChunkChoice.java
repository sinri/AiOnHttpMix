package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.chunk;

import io.github.sinri.AiOnHttpMix.azure.openai.core.filter.OpenAIContentFilterChoiceResults;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface OpenAIChatGptResponseChunkChoice extends UnmodifiableJsonifiableEntity {
    static OpenAIChatGptResponseChunkChoice wrap(JsonObject jsonObject) {
        return new OpenAIChatGptResponseChunkChoiceImpl(jsonObject);
    }

    default OpenAIContentFilterChoiceResults getContentFilterResults() {
        JsonObject contentFilterResults = readJsonObject("content_filter_results");
        if (contentFilterResults == null) return null;
        return OpenAIContentFilterChoiceResults.wrap(contentFilterResults);
    }

    @Nullable
    OpenAIChatGptResponseChunkChoiceDelta getDelta();

    @Nullable
    default String getFinishReason() {
        return readString("finish_reason");
    }

    default Integer getIndex() {
        return readInteger("index");
    }
}
