package io.github.sinri.AiOnHttpMix.utils.tools;

import io.vertx.json.schema.common.dsl.SchemaType;

public class FunctionParameterDefinition {
    private SchemaType type;

    private String name;

    private String description;

    public FunctionParameterDefinition(SchemaType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public SchemaType getType() {
        return type;
    }

    public void setType(SchemaType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
