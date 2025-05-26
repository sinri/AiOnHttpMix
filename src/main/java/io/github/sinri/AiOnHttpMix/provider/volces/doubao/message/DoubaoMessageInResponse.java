package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message;

import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.common.CommonToolCall;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface DoubaoMessageInResponse extends DoubaoMessage {

    static DoubaoMessageInResponse wrap(JsonObject jsonObject) {
        return new DoubaoMessageImpl(jsonObject);
    }

    /**
     * @return 内容输出的角色，此处固定为 assistant。
     */
    default String getRole() {
        return readString("role");
    }

    /**
     * @return 模型生成的消息内容。
     */
    default String getContent() {
        return readString("content");
    }

    /**
     * 仅深度推理模型支持返回此字段，深度推理模型请参见支持模型。
     *
     * @return 模型处理问题的思维链内容。
     */
    default String getReasoningContent() {
        return readString("reasoning_content");
    }

    /**
     * @return 模型生成的工具调用。
     */
    default List<ToolCall> getToolCalls() {
        List<JsonObject> a = readJsonObjectArray("tool_calls");
        if (a == null) return List.of();
        return a.stream().map(CommonToolCall::new)
                .map(ctc -> (ToolCall) ctc).toList();
    }
}
