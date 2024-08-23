package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.response;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.PromptFilterResults;
import io.github.sinri.AiOnHttpMix.azure.openai.core.impl.filter.PromptFilterResultsImpl;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class CreateChatCompletionResponseImpl extends UnmodifiableJsonifiableEntityImpl implements ChatGPTKit.CreateChatCompletionResponse {

    public CreateChatCompletionResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public CompletionUsage getUsage() {
        JsonObject usage = readJsonObject("usage");
        return new CompletionUsageImpl(Objects.requireNonNull(usage));
    }

    @Override
    public PromptFilterResults getPromptFilterResults() {
        JsonObject promptFilterResults = readJsonObject("prompt_filter_results");
        return new PromptFilterResultsImpl(Objects.requireNonNull(promptFilterResults));
    }

    @Override
    public List<Choice> getChoices() {
        List<Choice> choices = new ArrayList<>();
        List<JsonObject> list = readJsonObjectArray("choices");
        if (list != null) {
            list.forEach(item -> {
                if (item != null) {
                    var x = new ChoiceImpl(item);
                    choices.add(x);
                }
            });
        }
        return choices;
    }
}
