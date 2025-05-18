package io.github.sinri.AiOnHttpMix.provider.volces.doubao.request;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class ResponseFormatOptionsImpl extends JsonifiableEntityImpl<ResponseFormatOptions>
        implements ResponseFormatOptions {
    public ResponseFormatOptionsImpl() {
        super();
    }

    public ResponseFormatOptionsImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Nonnull
    @Override
    public ResponseFormatOptions getImplementation() {
        return this;
    }

    public static class JsonSchemaDefImpl extends JsonifiableEntityImpl<ResponseFormatOptions.JsonSchemaDef> implements JsonSchemaDef {
        public JsonSchemaDefImpl() {
            super();
        }

        public JsonSchemaDefImpl(JsonObject jsonObject) {
            super(jsonObject);
        }

        @Nonnull
        @Override
        public JsonSchemaDef getImplementation() {
            return this;
        }
    }
}
