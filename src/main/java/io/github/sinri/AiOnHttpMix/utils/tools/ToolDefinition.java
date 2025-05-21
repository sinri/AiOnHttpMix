package io.github.sinri.AiOnHttpMix.utils.tools;

import io.github.sinri.keel.core.json.JsonifiableEntity;

/**
 * 供大模型的工具调用功能选用的工具的定义。
 */
public interface ToolDefinition extends JsonifiableEntity<ToolDefinition> {
    /**
     * 工具的类型。
     * 一般来说，当前仅支持{@code function}。
     */
    default String type() {
        return readString("type");
    }

    /**
     * 设置工具的类型。
     *
     * @param type 工具的类型
     */
    default ToolDefinition type(String type) {
        return write("type", type);
    }

    /**
     * 在type为function的时候返回一个Function的实例。
     * 这也就是说，type不是function的时候，这个方法可能返回null。
     */
    FunctionToolDefinition function();

    /**
     * 在type为function的时候，设置所要调用的函数。
     *
     * @param functionToolDefinition 函数的定义
     */
    default ToolDefinition function(FunctionToolDefinition functionToolDefinition) {
        return type("function")
                .write("function", functionToolDefinition.toJsonObject());
    }
}
