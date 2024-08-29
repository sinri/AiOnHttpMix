package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface QwenVLInputMessage extends JsonifiableEntity<QwenVLInputMessage> {
    static QwenVLInputMessage create() {
        return new QwenVLInputMessageImpl();
    }

    static QwenVLInputMessage wrap(JsonObject json) {
        return new QwenVLInputMessageImpl(json);
    }

    default QwenVLRole getRole() {
        return QwenVLRole.valueOf(readString("role"));
    }

    default QwenVLInputMessage setRole(QwenVLRole role) {
        toJsonObject().put("role", role.name());
        return this;
    }

    default QwenVLInputMessage addContentItem(QwenVLMessageContentItem content) {
        JsonArray jsonArray = toJsonObject().getJsonArray("content");
        if (jsonArray == null) {
            jsonArray = new JsonArray();
            toJsonObject().put("content", jsonArray);
        }
        jsonArray.add(content.toJsonObject());
        return this;
    }


}
