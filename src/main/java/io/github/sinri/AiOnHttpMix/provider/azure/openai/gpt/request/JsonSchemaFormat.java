package io.github.sinri.AiOnHttpMix.provider.azure.openai.gpt.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface JsonSchemaFormat extends JsonifiableEntity<JsonSchemaFormat> {

    static JsonSchemaFormat create() {
        return new JsonSchemaFormatImpl();
    }

    static JsonSchemaFormat wrap(JsonObject jsonObject) {
        return new JsonSchemaFormatImpl(jsonObject);
    }

    default JsonSchemaFormat description(String description) {
        return write("description", description);
    }

    default String description() {
        return readString("description");
    }

    default JsonSchemaFormat name(String name) {
        return write(name, new JsonObject());
    }

    default String name() {
        return readString("name");
    }

    default JsonSchemaFormat schema(JsonObject schema) {
        return write("schema", schema);
    }

    default JsonObject schema() {
        return readJsonObject("schema");
    }

    default JsonSchemaFormat strict(boolean strict) {
        return write("strict", strict);
    }

    default Boolean strict() {
        return readBoolean("strict");
    }
}
