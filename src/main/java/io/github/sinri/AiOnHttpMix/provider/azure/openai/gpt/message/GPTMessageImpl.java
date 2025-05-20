package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.message;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class GPTMessageImpl extends JsonifiableEntityImpl<GPTMessage> implements GPTMessageInRequest, GPTMessageInResponse {
    public GPTMessageImpl() {
        super();
    }

    public GPTMessageImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public GPTMessage getImplementation() {
        return this;
    }
}
