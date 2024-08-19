package io.github.sinri.AiOnHttpMix.azure.chat;

import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import io.vertx.json.schema.draft7.dsl.Schemas;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ToolDefinition {
    private final JsonObject jsonObject;

    public ToolDefinition(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonObject toJsonObject() {
        return this.jsonObject;
    }

    public enum Type {
        function
    }

    /**
     *
     */
    public static class FunctionToolDefinitionBuilder {
        private final JsonObject functionJsonObject;
        private final ObjectSchemaBuilder propertiesSchemaJson = Schemas.objectSchema();

        /*
        {
            "type":"function",
            "function":{
                "name":"TargetFunctionA",
                "description":"Query recent weather for cities",
                "parameters":{
                    "type":"object",
                    "properties":{
                        "date":{
                            "description":"The date to get the weather for. The format is YYYY-MM-DD.",
                            "type":"string",
                            "$id":"urn:vertxschemas:4fc656a0-3701-48ec-bd9b-ea90818ec91b"
                        },
                        "city":{
                            "description":"City name to get the weather for.",
                            "type":"string",
                            "$id":"urn:vertxschemas:de20e57e-c98d-4539-a134-d39b9cdaeee1"
                        }
                    },
                    "$id":"urn:vertxschemas:91db6c3a-5c9b-47ed-9b8c-f8fe2dc2bfde"
                }
            }
         }
         */
        public FunctionToolDefinitionBuilder() {
            functionJsonObject = new JsonObject();
        }

        public FunctionToolDefinitionBuilder functionName(String functionName) {
            this.functionJsonObject.put("name", functionName);
            return this;
        }

        public FunctionToolDefinitionBuilder functionDescription(String functionDescription) {
            this.functionJsonObject.put("description", functionDescription);
            return this;
        }

        public FunctionToolDefinitionBuilder propertyAsString(String name, String desc) {
            return property(name, "string", desc);
        }

        public FunctionToolDefinitionBuilder propertyAsInt(String name, String desc) {
            return property(name, "int", desc);
        }

        public FunctionToolDefinitionBuilder propertyAsNumber(String name, String desc) {
            return property(name, "number", desc);
        }

        public FunctionToolDefinitionBuilder propertyAsBoolean(String name, String desc) {
            return property(name, "boolean", desc);
        }

        protected FunctionToolDefinitionBuilder property(String name, String type, String desc) {
            if (Objects.equals(type, "string")) {
                propertiesSchemaJson.property(name, Schemas.stringSchema()
                        .withKeyword("description", desc));
            } else if (Objects.equals(type, "int")) {
                propertiesSchemaJson.property(name, Schemas.intSchema()
                        .withKeyword("description", desc));
            } else if (Objects.equals(type, "number")) {
                propertiesSchemaJson.property(name, Schemas.numberSchema()
                        .withKeyword("description", desc));
            } else if (Objects.equals(type, "boolean")) {
                propertiesSchemaJson.property(name, Schemas.booleanSchema()
                        .withKeyword("description", desc));
            } else {
                throw new IllegalArgumentException();
            }
            return this;
        }

        public ToolDefinition build() {
            return new ToolDefinition(
                    new JsonObject()
                            .put("type", Type.function.name())
                            .put("function", functionJsonObject
                                    .put("parameters", propertiesSchemaJson.toJson())
                            )
            );
        }
    }
}
