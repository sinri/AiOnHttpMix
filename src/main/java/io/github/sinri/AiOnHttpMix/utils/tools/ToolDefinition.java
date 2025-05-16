package io.github.sinri.AiOnHttpMix.utils.tools;

import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface ToolDefinition extends JsonifiableEntity<ToolDefinition> {
    /**
     * tools的类型。
     * 一般来说，当前仅支持function。
     */
    default String type(){
        return readString("type");
    }

    default ToolDefinition type(String type) {
        return write("type", type);
    }

    /**
     * 在type为function的时候返回一个Function的实例。
     * 这也就是说，type不是function的时候，这个方法可能返回null。
     */
    FunctionToolDefinition function();

    default ToolDefinition function(FunctionToolDefinition functionToolDefinition) {
        return write("function", functionToolDefinition.toJsonObject());
    }
}
