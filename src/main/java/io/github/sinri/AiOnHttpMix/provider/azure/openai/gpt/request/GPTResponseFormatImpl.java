package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class GPTResponseFormatImpl extends JsonifiableEntityImpl<GPTResponseFormat> implements GPTResponseFormat {
    public GPTResponseFormatImpl() {
        super();
    }

    public GPTResponseFormatImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public GPTResponseFormat getImplementation() {
        return this;
    }
}
