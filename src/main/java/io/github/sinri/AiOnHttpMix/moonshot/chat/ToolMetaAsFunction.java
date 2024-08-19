package io.github.sinri.AiOnHttpMix.moonshot.chat;

import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import io.vertx.json.schema.draft7.dsl.Schemas;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ToolMetaAsFunction {
    private final JsonObject jsonObject;

    public ToolMetaAsFunction(@NotNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonObject toJsonObject() {
        return this.jsonObject;
    }

    public enum Type {
        function
    }

    public static class Builder {
        private final JsonObject functionJsonObject;
        private final ObjectSchemaBuilder propertiesSchemaJson = Schemas.objectSchema();

        public Builder() {
            functionJsonObject = new JsonObject();
        }

        public Builder functionName(String functionName) {
            this.functionJsonObject.put("name", functionName);
            return this;
        }

        public Builder functionDescription(String functionDescription) {
            this.functionJsonObject.put("description", functionDescription);
            return this;
        }

        public Builder propertyAsString(String name, String desc) {
            return property(name, "string", desc);
        }

        public Builder propertyAsInt(String name, String desc) {
            return property(name, "int", desc);
        }

        public Builder propertyAsNumber(String name, String desc) {
            return property(name, "number", desc);
        }

        public Builder propertyAsBoolean(String name, String desc) {
            return property(name, "boolean", desc);
        }

        protected Builder property(String name, String type, String desc) {
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

        public ToolMetaAsFunction build() {
            return new ToolMetaAsFunction(
                    new JsonObject()
                            .put("type", Type.function.name())
                            .put("function", functionJsonObject
                                    .put("parameters", propertiesSchemaJson.toJson())
                            )
            );
        }
    }
}
