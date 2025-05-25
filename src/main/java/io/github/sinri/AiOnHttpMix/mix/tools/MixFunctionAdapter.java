package io.github.sinri.AiOnHttpMix.mix.tools;

import io.github.sinri.AiOnHttpMix.utils.tools.FunctionParameterDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonFunctionToolDefinition;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolDefinition;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;
import java.util.List;

public interface MixFunctionAdapter {
    String getFunctionName();

    String getFunctionDescription();

    List<FunctionParameterDefinition> getParameters();

    default CommonFunctionToolDefinition toFunctionToolDefinition() {
        return new CommonFunctionToolDefinition(
                getFunctionName(),
                getFunctionDescription(),
                getParameters()
        );
    }

    default CommonToolDefinition toToolDefinition() {
        return new CommonToolDefinition(toFunctionToolDefinition());
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
