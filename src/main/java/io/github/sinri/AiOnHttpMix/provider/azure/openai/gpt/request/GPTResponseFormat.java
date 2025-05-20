package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface GPTResponseFormat extends JsonifiableEntity<GPTResponseFormat> {
    static GPTResponseFormat wrap(JsonObject jsonObject) {
        return new GPTResponseFormatImpl(jsonObject);
    }

    static GPTResponseFormat createAsText() {
        return new GPTResponseFormatImpl(new JsonObject()
                .put("type", "text"));
    }

    static GPTResponseFormat createAsJsonObject() {
        return new GPTResponseFormatImpl(new JsonObject()
                .put("type", "json_object"));
    }

    static GPTResponseFormat createAsJsonSchema(JsonSchemaFormat jsonSchemaFormat) {
        return new GPTResponseFormatImpl(new JsonObject()
                .put("type", "json_schema")
                .put("json_schema", jsonSchemaFormat.toJsonObject()));
    }

}
