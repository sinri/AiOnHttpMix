package io.github.sinri.AiOnHttpMix.mix.tools;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionParameterDefinition;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;
import java.util.List;

public interface MixFunctionAdapter {
    String getFunctionName();

    String getFunctionDescription();

    List<FunctionParameterDefinition> getParameters();

    default MixFunctionToolDefinition toFunctionToolDefinition() {
        MixFunctionToolDefinition functionToolDefinition = new MixFunctionToolDefinition();
        functionToolDefinition.name(getFunctionName());
        functionToolDefinition.description(getFunctionDescription());
        functionToolDefinition.parameters(getParameters());
        return functionToolDefinition;
    }

    default MixToolDefinition toToolDefinition() {
        return new MixToolDefinition(toFunctionToolDefinition());
    }

    /**
     * Call function as LLM required, along with a fixed argument according to running context.
     *
     * @param arguments     the arguments parsed from LLM response
     * @param fixedArgument the argument object from context
     * @since 1.2.6
     */
    Future<String> call(@Nullable JsonObject arguments, @Nullable JsonObject fixedArgument);

}
