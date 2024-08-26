package io.github.sinri.AiOnHttpMix.volces.v3.mixin.response;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesKit;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface VolcesChatResponseMessageMixin extends UnmodifiableJsonifiableEntity {
    /**
     * @return 固定为 assistant
     */
    default VolcesKit.ChatRole getRole() {
        return VolcesKit.ChatRole.valueOf(readString("role"));
    }

    /**
     * @return 模型生成的消息内容
     */
    @Nullable
    default String getContent() {
        return readString("content");
    }

    /**
     * content 与 tool_calls 字段二者至少有一个为非空
     *
     * @return 模型生成的工具调用
     */
    @Nullable
    default List<VolcesKit.ChatCompletionsResponse.ToolCall> getToolCalls() {
        List<JsonObject> array = readJsonObjectArray("tool_calls");
        if (array == null) return null;
        List<VolcesKit.ChatCompletionsResponse.ToolCall> toolCalls = new ArrayList<>();
        array.forEach(x -> {
            var y = VolcesKit.ChatCompletionsResponse.ToolCall.wrap(x);
            toolCalls.add(y);
        });
        return toolCalls;
    }
}
