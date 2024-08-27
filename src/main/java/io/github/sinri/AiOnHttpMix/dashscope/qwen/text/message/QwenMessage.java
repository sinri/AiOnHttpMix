package io.github.sinri.AiOnHttpMix.dashscope.qwen.text.message;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenRole;
import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.tool.QwenToolCall;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface QwenMessage extends JsonifiableEntity<QwenMessage> {
    static QwenMessage create() {
        return new QwenMessageImpl();
    }

    static QwenMessage wrap(JsonObject jsonObject) {
        return new QwenMessageImpl(jsonObject);
    }

    default QwenMessage setContent(String content) {
        this.toJsonObject().put("content", content);
        return this;
    }

    /**
     * role为tool表示当前message为function_call的调用结果，
     * name是工具函数名，需要和上轮response中的tool_calls[i].function.name参数保持一致，
     * content为工具函数的输出。
     */
    default QwenMessage setName(String name) {
        this.toJsonObject().put("name", name);
        return this;
    }

    default QwenRole getRole() {
        return QwenRole.valueOf(readString("role"));
    }

    default QwenMessage setRole(QwenRole role) {
        this.toJsonObject().put("role", role.name());
        return this;
    }

    default String getContent() {
        return readString("content");
    }

    @Nullable
    default String getName() {
        return readString("name");
    }

    @Nullable
    List<QwenToolCall> getToolCalls();
}