package io.github.sinri.AiOnHttpMix.utils.tools.common;

import io.github.sinri.AiOnHttpMix.utils.StreamPieceCollector;
import io.github.sinri.AiOnHttpMix.utils.tools.FunctionToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.vertx.core.json.JsonObject;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class ToolCallStreamPieceCollector implements StreamPieceCollector<ToolCall, ToolCall> {
    private final FunctionToolCallStreamPieceCollector functionToolCallBuffer = new FunctionToolCallStreamPieceCollector();
    ;
    private String toolCallId;
    private Integer index;
    private String type;


    public void accept(ToolCall toolCall) {
        String id = toolCall.getId();
        if (!Keel.stringHelper().isNullOrBlank(id)) {
            toolCallId = id;
        }
        index = toolCall.getIndex();
        type = toolCall.getType();
        FunctionToolCall function = toolCall.getFunction();
        functionToolCallBuffer.accept(function);
    }

    @Override
    public ToolCall build() {
        return new CommonToolCall(new JsonObject()
                .put("id", toolCallId)
                .put("index", index)
                .put("type", type)
                .put("function", functionToolCallBuffer.build().cloneAsJsonObject())
        );
    }
}
