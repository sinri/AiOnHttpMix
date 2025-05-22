package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.tool.QwenToolCall;
import io.github.sinri.AiOnHttpMix.utils.tools.ToolCall;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 2.0.0
 */
public interface QwenMessageInResponse extends QwenMessage {
    static QwenMessageInResponse wrap(JsonObject jsonObject) {
        return new QwenMessageImpl(jsonObject);
    }

    /**
     * 输出消息的角色，固定为assistant。
     */
    default String getRole() {
        return readString("role");
    }

    /**
     * 输出消息的内容。
     * 当使用qwen-vl或qwen-audio系列模型时为array，其余情况为string。
     * 如果发起Function Calling，则该值为空。
     */
    default String getContent() {
        return readString("content");
    }

    /**
     * 输出消息的内容。
     *
     * @return 当使用qwen-vl或qwen-audio系列模型时为array，抽取其中的元素的text字段组成列表。
     */
    default List<String> getContents() {
        var x = readJsonObjectArray("content");
        if (x == null) return List.of();
        return x.stream().map(j -> j.getString("text")).toList();
    }

    /**
     * @return QwQ 模型、QVQ模型的深度思考内容。
     */
    default String getReasoningContent() {
        return readString("reasoning_content");
    }

    /**
     * 如果模型需要调用工具，则会生成tool_calls参数。
     */
    default List<ToolCall> getToolCalls() {
        List<JsonObject> x = readJsonObjectArray("tool_calls");
        if (x == null) return List.of();
        return x.stream().map(QwenToolCall::new).collect(Collectors.toUnmodifiableList());
    }
}
