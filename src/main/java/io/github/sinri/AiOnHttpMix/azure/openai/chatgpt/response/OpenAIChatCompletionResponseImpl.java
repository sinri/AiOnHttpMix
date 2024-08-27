package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.AiOnHttpMix.azure.openai.core.filter.OpenAIPromptFilterResults;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class OpenAIChatCompletionResponseImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIChatGptResponse {

    public OpenAIChatCompletionResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public OpenAIChatGptCompletionUsage getUsage() {
        JsonObject usage = readJsonObject("usage");
        return new OpenAIChatGptCompletionUsageImpl(Objects.requireNonNull(usage));
    }

    @Override
    public OpenAIPromptFilterResults getPromptFilterResults() {
        JsonObject promptFilterResults = readJsonObject("prompt_filter_results");
        return OpenAIPromptFilterResults.wrap(Objects.requireNonNull(promptFilterResults));
    }

    @Override
    public List<OpenAIChatGptResponseChoice> getChoices() {
        List<OpenAIChatGptResponseChoice> choices = new ArrayList<>();
        List<JsonObject> list = readJsonObjectArray("choices");
        if (list != null) {
            list.forEach(item -> {
                if (item != null) {
                    var x = new OpenAIChatGptResponseChoiceImpl(item);
                    choices.add(x);
                }
            });
        }
        return choices;
    }
}
