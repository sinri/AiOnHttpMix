package io.github.sinri.AiOnHttpMix.dashscope.conversation;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;

public class Message extends SimpleJsonifiableEntity {

    public Message(JsonObject jsonObject) {
        super(jsonObject);
    }

    public Message(Role role, String content) {
        super();
        setContent(content);
        setRole(role);
    }

    public String getContent() {
        return readString("content");
    }

    public Message setContent(String content) {
        this.jsonObject.put("content", content);
        return this;
    }

    public Role getRole() {
        return Role.valueOf(readString("role"));
    }

    public Message setRole(Role role) {
        this.jsonObject.put("role", role.name());
        return this;
    }

    /**
     * role为tool表示当前message为function_call的调用结果，
     * name是function的名称，需要和上轮response中的tool_calls[i].function.name参数保持一致，
     * content为function的输出。
     *
     * @return function_call的调用结果里的function的名称
     */
    public String getName() {
        return readString("name");
    }

    public Message setName(String name) {
        this.jsonObject.put("name", name);
        return this;
    }
}
