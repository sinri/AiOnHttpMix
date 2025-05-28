package io.github.sinri.AiOnHttpMix.mix.tools;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionParameterDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;
import java.util.List;

/**
 * MixFunctionAdapter 接口定义了工具适配器的通用规范，
 * 用于将不同类型的工具（如函数、工具调用等）适配为统一的 MixFunctionToolDefinition 结构。
 * <p>
 * 该接口提供了获取工具名称、描述、参数等基本信息的方法，
 * 并支持将工具定义转换为 CommonFunctionToolDefinition 和 CommonToolDefinition 对象。
 * </p>
 */
public interface MixFunctionAdapter {
    /**
     * 获取工具名称。
     * @return 工具名称
     */
    String getFunctionName();

    /**
     * 获取工具描述。
     * @return 工具描述
     */
    String getFunctionDescription();

    /**
     * 获取工具参数列表。
     * @return 工具参数列表
     */
    List<FunctionParameterDefinition> getParameters();

    /**
     * 将工具定义转换为 CommonFunctionToolDefinition 对象。
     * @return CommonFunctionToolDefinition 对象
     */
    default CommonFunctionToolDefinition toFunctionToolDefinition() {
        return new CommonFunctionToolDefinition(
                getFunctionName(),
                getFunctionDescription(),
                getParameters()
        );
    }

    /**
     * 将工具定义转换为 CommonToolDefinition 对象。
     * @return CommonToolDefinition 对象
     */
    default CommonToolDefinition toToolDefinition() {
        return new CommonToolDefinition(toFunctionToolDefinition());
    }

    /**
     * Call function as LLM required, along with a fixed argument according to running context.
     *
     * @param arguments     the arguments parsed from LLM response
     * @param fixedArgument the argument object from context
     */
    Future<String> call(@Nullable JsonObject arguments, @Nullable JsonObject fixedArgument);

}
