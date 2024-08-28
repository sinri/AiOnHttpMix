package io.github.sinri.AiOnHttpMix.volces.v3.response;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface VolcesChatResponseMessage extends UnmodifiableJsonifiableEntity {
    static VolcesChatResponseMessage wrap(JsonObject jsonObject) {
        return new VolcesChatResponseMessageImpl(jsonObject);
    }
    /**
     * @return 固定为 assistant
     */
    default VolcesChatRole getRole() {
        return VolcesChatRole.valueOf(readString("role"));
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
    default List<VolcesChatMessageToolCallForResponse> getToolCalls() {
        List<JsonObject> array = readJsonObjectArray("tool_calls");
        if (array == null) return null;
        List<VolcesChatMessageToolCallForResponse> toolCalls = new ArrayList<>();
        array.forEach(x -> {
            var y = VolcesChatMessageToolCallForResponse.wrap(x);
            toolCalls.add(y);
        });
        return toolCalls;
    }
}
