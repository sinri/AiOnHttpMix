package io.github.sinri.AiOnHttpMix.utils.tools;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import io.vertx.json.schema.common.dsl.Schemas;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * 一个供调用的函数的定义。
 */
public interface FunctionToolDefinition extends JsonifiableEntity<FunctionToolDefinition> {
    /**
     * @return 工具函数的名称
     */
    default String name() {
        return readString("name");
    }

    /**
     * @param name 工具函数的名称
     */
    default FunctionToolDefinition name(String name) {
        return write("name", name);
    }

    /**
     * @return 工具函数的描述，供模型选择何时以及如何调用工具函数。
     */
    default String description() {
        return readString("description");
    }

    /**
     * @param description 工具函数的描述，供模型选择何时以及如何调用工具函数。
     */
    default FunctionToolDefinition description(String description) {
        return write("description", description);
    }


    /**
     * @return 工具的参数描述，需要是一个合法的JSON Schema。
     *         如果parameters参数为空，表示function没有入参。
     * @see <a href="https://json-schema.org/understanding-json-schema">JSON Schema</a>
     */
    default JsonObject parameters() {
        return readJsonObject("parameters");
    }

    /**
     * @param parameters 工具的参数描述，需要是一个合法的JSON Schema。
     * @see <a href="https://json-schema.org/understanding-json-schema">JSON Schema</a>
     */
    default FunctionToolDefinition parameters(@Nullable JsonObject parameters) {
        return write("parameters", parameters);
    }

    /**
     * @param handler 给定一个JSON Schema的Builder，在此处理器里将其完善为工具的参数描述。
     */
    default FunctionToolDefinition parameters(@Nonnull Handler<ObjectSchemaBuilder> handler) {
        ObjectSchemaBuilder objectSchemaBuilder = Schemas.objectSchema();
        handler.handle(objectSchemaBuilder);
        return parameters(objectSchemaBuilder.toJson());
    }

    default FunctionToolDefinition parameters(@Nonnull List<FunctionParameterDefinition> parameterDefinitions) {
        if (parameterDefinitions.isEmpty()) {
            return parameters((JsonObject) null);
        }
        return parameters(builder -> {
            parameterDefinitions.forEach(parameterDefinition -> {
                builder.property(
                        parameterDefinition.getName(),
                        Schemas.schema()
                               .type(parameterDefinition.getType())
                               .withKeyword("description", parameterDefinition.getDescription())
                );
            });
        });
    }
}
