package io.github.sinri.AiOnHttpMix.utils.tools;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface FunctionToolDefinition extends JsonifiableEntity<FunctionToolDefinition> {
    /**
     * @return 工具函数的名称
     */
    default String name() {
        return readString("name");
    }

    default FunctionToolDefinition name(String name) {
        return write("name", name);
    }

    /**
     * @return 工具函数的描述，供模型选择何时以及如何调用工具函数。
     */
    default String description() {
        return readString("description");
    }

    default FunctionToolDefinition description(String descriptions) {
        return write("description", descriptions);
    }


    /**
     * @return 工具的参数描述，需要是一个合法的JSON Schema。如果parameters参数为空，表示function没有入参。
     * @see <a href="https://json-schema.org/understanding-json-schema">JSON Schema</a>
     */
    default JsonObject parameters() {
        return readJsonObject("parameters");
    }

    default FunctionToolDefinition parameters(JsonObject parameters) {
        return write("parameters", parameters);
    }

}
