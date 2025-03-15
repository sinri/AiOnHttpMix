package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @since 1.1.2
 */
public interface FunctionCallAdapter {
    @NotNull
    String getFunctionName();

    @NotNull
    String getFunctionDescription();

    @NotNull List<FunctionToolArgumentDefinition> getArguments();

    @NotNull
    default AnyLLMFunctionToolDefinition toFunctionToolDefinition() {
        AnyLLMFunctionToolDefinition.Builder builder = AnyLLMFunctionToolDefinition.builder();
        builder.functionName(getFunctionName()).functionDescription(getFunctionDescription());
        List<FunctionToolArgumentDefinition> arguments = getArguments();
        arguments.forEach(builder::property);
        return builder.build();
    }

    /**
     * @param arguments as of 1.2.6 nullable
     * @deprecated use {@link FunctionCallAdapter#callFunction(JsonObject, JsonObject)}.
     */
    @NotNull
    @Deprecated(since = "1.2.6", forRemoval = true)
    default <R> Future<R> callFunction(@Nullable JsonObject arguments) {
        return callFunction(arguments, null);
    }

    /**
     * Call function as LLM required, along with a fixed argument according to running context.
     *
     * @param arguments     the arguments parsed from LLM response
     * @param fixedArgument the argument object from context
     * @since 1.2.6
     */
    @NotNull
    <R> Future<R> callFunction(@Nullable JsonObject arguments, @Nullable JsonObject fixedArgument);


}
