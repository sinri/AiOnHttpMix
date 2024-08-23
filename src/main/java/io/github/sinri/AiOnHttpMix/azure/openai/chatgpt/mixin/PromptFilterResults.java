package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;

public interface PromptFilterResults extends UnmodifiableJsonifiableEntity {
    default Integer getPromptIndex() {
        return readInteger("prompt_index");
    }

    /**
     * Information about the content filtering category (hate, sexual, violence, self_harm),
     * if it has been detected,
     * as well as the severity level (very_low, low, medium, high-scale that determines the intensity and risk level of harmful content)
     * and if it has been filtered or not.
     * Information about jailbreak content and profanity, if it has been detected, and if it has been filtered or not.
     * And information about customer blocklist, if it has been filtered and its id.
     */
    ContentFilterPromptResults getContentFilterResults();

}
