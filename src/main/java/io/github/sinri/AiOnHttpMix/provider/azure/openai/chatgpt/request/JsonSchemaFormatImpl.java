package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class JsonSchemaFormatImpl extends JsonifiableEntityImpl<JsonSchemaFormat> implements JsonSchemaFormat {
    public JsonSchemaFormatImpl() {
        super();
    }

    public JsonSchemaFormatImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public JsonSchemaFormat getImplementation() {
        return this;
    }
}
