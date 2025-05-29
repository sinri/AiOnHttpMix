package io.github.sinri.AiOnHttpMix.utils.tools.common;

import io.github.sinri.AiOnHttpMix.utils.StreamPieceCollector;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.vertx.core.json.JsonObject;

public class FunctionToolCallStreamPieceCollector implements StreamPieceCollector<FunctionToolCall, FunctionToolCall> {
    private final StringBuilder nameBuffer = new StringBuilder();
    private final StringBuilder argumentsBuffer = new StringBuilder();

    public void accept(FunctionToolCall functionToolCall) {
        String name = functionToolCall.getName();
        if (name != null) {
            nameBuffer.append(name);
        }
        String arguments = functionToolCall.getArguments();
        if (arguments != null) {
            argumentsBuffer.append(arguments);
        }
    }

    @Override
    public CommonFunctionToolCall build() {
        return new CommonFunctionToolCall(new JsonObject()
                .put("name", nameBuffer.toString())
                .put("arguments", argumentsBuffer.toString())
        );
    }
}
