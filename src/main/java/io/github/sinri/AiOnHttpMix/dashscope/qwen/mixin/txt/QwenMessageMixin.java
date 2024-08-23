package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.txt;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.QwenKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface QwenMessageMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {
    enum Role {
        system, user, assistant, tool
    }

    default E setRole(Role role) {
        this.toJsonObject().put("role", role.name());
        return getImplementation();
    }

    default E setContent(String content) {
        this.toJsonObject().put("content", content);
        return getImplementation();
    }

    /**
     * role为tool表示当前message为function_call的调用结果，
     * name是工具函数名，需要和上轮response中的tool_calls[i].function.name参数保持一致，
     * content为工具函数的输出。
     */
    default E setName(String name) {
        this.toJsonObject().put("name", name);
        return getImplementation();
    }

    default Role getRole() {
        return Role.valueOf(readString("role"));
    }

    default String getContent() {
        return readString("content");
    }

    @Nullable
    default String getName() {
        return readString("name");
    }

    @Nullable
    List<QwenKit.ToolCall> getToolCalls();
}