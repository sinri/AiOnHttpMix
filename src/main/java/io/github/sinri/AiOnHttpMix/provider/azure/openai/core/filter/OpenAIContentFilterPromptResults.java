package io.github.sinri.AiOnHttpMix.provider.azure.openai.core.filter;

import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;

public interface OpenAIContentFilterPromptResults extends OpenAIContentFilterResultsBase {
    static OpenAIContentFilterPromptResults wrap(JsonObject jsonObject) {
        return new ContentFilterPromptResultsImpl(jsonObject);
    }

    @Nullable
    OpenAIContentFilterDetectedResult getJailbreak();

    default boolean whetherFiltered() {
        var hate = getHate();
        var profanity = getProfanity();
        var sexual = getSexual();
        var jailbreak = getJailbreak();
        var violence = getViolence();
        var selfHarm = getSelfHarm();

        if (hate != null && hate.isFiltered()) {
            return true;
        }
        if (profanity != null && profanity.isFiltered()) {
            return true;
        }
        if (sexual != null && sexual.isFiltered()) {
            return true;
        }
        if (jailbreak != null && jailbreak.isFiltered()) {
            return true;
        }
        if (violence != null && violence.isFiltered()) {
            return true;
        }
        return selfHarm != null && selfHarm.isFiltered();
    }

}
