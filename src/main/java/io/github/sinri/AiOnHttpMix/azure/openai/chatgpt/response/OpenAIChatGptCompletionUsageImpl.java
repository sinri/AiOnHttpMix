package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

final class OpenAIChatGptCompletionUsageImpl extends UnmodifiableJsonifiableEntityImpl implements OpenAIChatGptCompletionUsage {
    public OpenAIChatGptCompletionUsageImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
