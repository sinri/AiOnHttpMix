package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class GPTRequestImpl extends JsonifiableEntityImpl<GPTRequest> implements GPTRequest {
    public GPTRequestImpl() {
        super();
    }

    public GPTRequestImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public GPTRequest getImplementation() {
        return this;
    }
}
