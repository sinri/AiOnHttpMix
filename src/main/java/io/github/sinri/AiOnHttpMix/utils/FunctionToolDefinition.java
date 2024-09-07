package io.github.sinri.AiOnHttpMix.utils;

import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.JsonSchema;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import io.vertx.json.schema.draft7.dsl.Schemas;

import java.util.Objects;

public interface FunctionToolDefinition<E> extends JsonifiableEntity<E>, SelfInterface<E> {

    default String getType() {
        return readString("type");
    }

    default JsonSchema getFunction() {
        JsonObject x = readJsonObject("function");
        if (x == null) return null;
        return JsonSchema.of(x);
    }

    abstract class FunctionToolDefinitionBuilder<B, D> implements SelfInterface<B> {
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

        public B functionName(String functionName) {
            this.functionJsonObject.put("name", functionName);
            return getImplementation();
        }

        public B functionDescription(String functionDescription) {
            this.functionJsonObject.put("description", functionDescription);
            return getImplementation();
        }

        public B propertyAsString(String name, String desc) {
            return property(name, "string", desc);
        }

        public B propertyAsInt(String name, String desc) {
            return property(name, "int", desc);
        }

        public B propertyAsNumber(String name, String desc) {
            return property(name, "number", desc);
        }

        public B propertyAsBoolean(String name, String desc) {
            return property(name, "boolean", desc);
        }

        protected B property(String name, String type, String desc) {
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
            return getImplementation();
        }

        public JsonObject toJsonObject() {
            return new JsonObject()
                    .put("type", "function")
                    .put("function", functionJsonObject
                            .put("parameters", propertiesSchemaJson.toJson())
                    );
        }

        abstract public D build();
    }
}
