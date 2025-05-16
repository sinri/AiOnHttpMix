package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.request.input;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.message.QwenMessage;
import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.entity.message.QwenMessageInRequest;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface QwenRequestInput extends JsonifiableEntity<QwenRequestInput> {
    /**
     * Create a new QwenRequestInput instance.
     *
     * @return a new QwenRequestInput instance
     */
    static QwenRequestInput create() {
        return new QwenRequestInputImpl();
    }

    /**
     * Wrap an existing JsonObject as QwenRequestInput.
     *
     * @param jsonObject the JsonObject to wrap
     * @return a QwenRequestInput instance wrapping the given JsonObject
     */
    static QwenRequestInput wrap(JsonObject jsonObject) {
        return new QwenRequestInputImpl(jsonObject);
    }

    /**
     * 向由历史对话组成的消息列表中新增一个消息。
     */
    default QwenRequestInput addMessage(QwenMessage message) {
        JsonArray array = this.ensureJsonArray("messages");
        array.add(message.toJsonObject());
        return this.getImplementation();
    }

    /**
     * @return 由历史对话组成的消息列表
     */
    default List<QwenMessageInRequest> getMessages() {
        List<JsonObject> messages = this.readJsonObjectArray("messages");
        if (messages == null) {
            return List.of();
        }
        return messages.stream().map(QwenMessageInRequest::wrap).toList();
    }
}
