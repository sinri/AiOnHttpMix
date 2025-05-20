package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class GPTResponseChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements GPTResponseChoice {
    public GPTResponseChoiceImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
