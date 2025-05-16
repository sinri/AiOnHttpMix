package io.github.sinri.AiOnHttpMix.utils.tools;

public interface ToolCall {
    /**
     * 工具类型，固定为function。
     */
    String getType();

    /**
     * 本次工具响应的ID。
     */
    String getId();

    /**
     * 当前tool_calls对象在tool_calls数组中的索引。
     */
    Integer getIndex();

    /**
     * 调用工具的名称，以及输入参数。
     */
    FunctionToolCall getFunction();
}
