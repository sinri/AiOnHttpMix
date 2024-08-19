package io.github.sinri.AiOnHttpMix.moonshot.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public class Usage extends SimpleJsonifiableEntity {
    public Usage(JsonObject jsonObject) {
        super(jsonObject);
    }

    public Integer getPromptTokens() {
        return readInteger("prompt_tokens");
    }

    public Integer getCompletionTokens() {
        return readInteger("completion_tokens");
    }

    public Integer getTotalTokens() {
        return readInteger("total_tokens");
    }
}
