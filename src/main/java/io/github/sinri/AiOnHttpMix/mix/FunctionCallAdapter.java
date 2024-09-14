package io.github.sinri.AiOnHttpMix.mix;

import io.github.sinri.AiOnHttpMix.utils.FunctionToolArgumentDefinition;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    Future<Object> callFunction(JsonObject arguments);


}
