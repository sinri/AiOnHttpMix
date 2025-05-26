package io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message;

import io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.message.vision.QwenVisionContent;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface QwenMessageInVisionRequest extends QwenMessage {
    static QwenMessageInVisionRequest create() {
        return new QwenMessageImpl();
    }

    static QwenMessageInVisionRequest wrap(JsonObject jsonObject) {
        return new QwenMessageImpl(jsonObject);
    }

    /**
     * 用户发送给模型的消息。
     * 如果您的输入只有文本，则为string类型；如果您的输入包含图像等多模态数据，则为array类型。
     *
     * @param contentList 用户消息的内容。
     */
    static QwenMessage createAsUserInRequest(List<QwenVisionContent> contentList) {
        return create()
                .write("content", new JsonArray(contentList.stream().map(JsonifiableEntity::toJsonObject).toList()))
                .write("role", "user");
    }

}
