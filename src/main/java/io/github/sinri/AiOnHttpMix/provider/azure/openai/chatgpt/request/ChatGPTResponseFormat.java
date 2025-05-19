package io.github.sinri.AiOnHttpMix.provider.azure.openai.chatgpt.request;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface ChatGPTResponseFormat extends JsonifiableEntity<ChatGPTResponseFormat> {
    static ChatGPTResponseFormat wrap(JsonObject jsonObject) {
        return new ChatGPTResponseFormatImpl(jsonObject);
    }

    static ChatGPTResponseFormat createAsText() {
        return new ChatGPTResponseFormatImpl(new JsonObject()
                .put("type", "text"));
    }

    static ChatGPTResponseFormat createAsJsonObject() {
        return new ChatGPTResponseFormatImpl(new JsonObject()
                .put("type", "json_object"));
    }

    static ChatGPTResponseFormat createAsJsonSchema(JsonSchemaFormat jsonSchemaFormat) {
        return new ChatGPTResponseFormatImpl(new JsonObject()
                .put("type", "json_schema")
                .put("json_schema", jsonSchemaFormat.toJsonObject()));
    }

}
