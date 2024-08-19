package io.github.sinri.AiOnHttpMix.dashscope.conversation;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import io.vertx.json.schema.draft7.dsl.Schemas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToolMeta extends SimpleJsonifiableEntity {
    public ToolMeta() {
        super();
        this.setType(ToolType.function);
    }

    public ToolMeta setType(ToolType type) {
        this.jsonObject.put("type", type.name());
        return this;
    }

    /**
     * @param name        function的名称，必须是字母、数字，或包含下划线和短划线，最大长度为64。
     * @param description function的描述，供模型选择何时以及如何调用function。
     * @param parameters  表示function的参数描述，需要是一个合法的json schema。缺省表示function没有入参。
     */
    public ToolMeta setFunction(
            @NotNull String name,
            @NotNull String description,
            @Nullable JsonObject parameters
    ) {
        var f = new JsonObject()
                .put("name", name)
                .put("description", description);
        if (parameters != null)
            f.put("parameters", parameters);
        this.jsonObject.put("function", f);
        return this;
    }

    public ToolMeta setFunction(
            @NotNull String name,
            @NotNull String description,
            @Nullable List<FunctionArgument> parameters
    ) {
        JsonObject jsonSchema = null;
        if (parameters != null) {
            ObjectSchemaBuilder objectSchemaBuilder = Schemas.objectSchema();
            parameters.forEach(p -> {
                if (p.required) {
                    objectSchemaBuilder.requiredProperty(p.argumentName, Schemas.stringSchema()
                            .withKeyword("description", p.argumentDesc)
                    );
                } else {
                    objectSchemaBuilder.property(p.argumentName, Schemas.stringSchema()
                            .withKeyword("description", p.argumentDesc)
                    );
                }
            });
            jsonSchema = objectSchemaBuilder.toJson();
        }
        return setFunction(name, description, jsonSchema);
    }

    /**
     * 表示tools的类型
     */
    public enum ToolType {
        function
    }

    public record FunctionArgument(String argumentName, String argumentType, String argumentDesc, boolean required) {
    }
}
