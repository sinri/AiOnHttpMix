package io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.impl.request;

import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.ChatGPTKit;
import io.github.sinri.AiOnHttpMix.azure.openai.chatgpt.mixin.OpenAIToolDefinitionMixin;
import io.github.sinri.AiOnHttpMix.utils.FunctionToolDefinition;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import io.vertx.json.schema.draft7.dsl.Schemas;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ToolDefinitionImpl implements ChatGPTKit.ToolDefinition {
    private JsonObject jsonObject;

    public ToolDefinitionImpl(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public @NotNull JsonObject toJsonObject() {
        return this.jsonObject;
    }

    @Override
    public ChatGPTKit.@NotNull ToolDefinition reloadDataFromJsonObject(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public static class Builder extends FunctionToolDefinition.FunctionToolDefinitionBuilder<Builder, ToolDefinitionImpl> {

        @Override
        public ToolDefinitionImpl build() {
            return new ToolDefinitionImpl(toJsonObject());
        }

        @Override
        public @NotNull Builder getImplementation() {
            return this;
        }
    }

//    @Deprecated
//    public static class FunctionToolDefinitionBuilder implements OpenAIToolDefinitionMixin.FunctionToolDefinitionBuilder {
//        private final JsonObject functionJsonObject;
//        private final ObjectSchemaBuilder propertiesSchemaJson = Schemas.objectSchema();
//
//        /*
//        {
//            "type":"function",
//            "function":{
//                "name":"TargetFunctionA",
//                "description":"Query recent weather for cities",
//                "parameters":{
//                    "type":"object",
//                    "properties":{
//                        "date":{
//                            "description":"The date to get the weather for. The format is YYYY-MM-DD.",
//                            "type":"string",
//                            "$id":"urn:vertxschemas:4fc656a0-3701-48ec-bd9b-ea90818ec91b"
//                        },
//                        "city":{
//                            "description":"City name to get the weather for.",
//                            "type":"string",
//                            "$id":"urn:vertxschemas:de20e57e-c98d-4539-a134-d39b9cdaeee1"
//                        }
//                    },
//                    "$id":"urn:vertxschemas:91db6c3a-5c9b-47ed-9b8c-f8fe2dc2bfde"
//                }
//            }
//         }
//         */
//        public FunctionToolDefinitionBuilder() {
//            functionJsonObject = new JsonObject();
//        }
//
//        public FunctionToolDefinitionBuilder functionName(String functionName) {
//            this.functionJsonObject.put("name", functionName);
//            return this;
//        }
//
//        public FunctionToolDefinitionBuilder functionDescription(String functionDescription) {
//            this.functionJsonObject.put("description", functionDescription);
//            return this;
//        }
//
//        public FunctionToolDefinitionBuilder propertyAsString(String name, String desc) {
//            return property(name, "string", desc);
//        }
//
//        public FunctionToolDefinitionBuilder propertyAsInt(String name, String desc) {
//            return property(name, "int", desc);
//        }
//
//        public FunctionToolDefinitionBuilder propertyAsNumber(String name, String desc) {
//            return property(name, "number", desc);
//        }
//
//        public FunctionToolDefinitionBuilder propertyAsBoolean(String name, String desc) {
//            return property(name, "boolean", desc);
//        }
//
//        protected FunctionToolDefinitionBuilder property(String name, String type, String desc) {
//            if (Objects.equals(type, "string")) {
//                propertiesSchemaJson.property(name, Schemas.stringSchema()
//                        .withKeyword("description", desc));
//            } else if (Objects.equals(type, "int")) {
//                propertiesSchemaJson.property(name, Schemas.intSchema()
//                        .withKeyword("description", desc));
//            } else if (Objects.equals(type, "number")) {
//                propertiesSchemaJson.property(name, Schemas.numberSchema()
//                        .withKeyword("description", desc));
//            } else if (Objects.equals(type, "boolean")) {
//                propertiesSchemaJson.property(name, Schemas.booleanSchema()
//                        .withKeyword("description", desc));
//            } else {
//                throw new IllegalArgumentException();
//            }
//            return this;
//        }
//
//        public ToolDefinitionImpl build() {
//            return new ToolDefinitionImpl(
//                    new JsonObject()
//                            .put("type", Type.function.name())
//                            .put("function", functionJsonObject
//                                    .put("parameters", propertiesSchemaJson.toJson())
//                            )
//            );
//        }
//    }
}
