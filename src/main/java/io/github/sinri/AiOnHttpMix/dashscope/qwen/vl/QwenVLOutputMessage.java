package io.github.sinri.AiOnHttpMix.dashscope.qwen.vl;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface QwenVLOutputMessage extends UnmodifiableJsonifiableEntity {
    static QwenVLOutputMessage wrap(JsonObject jsonObject) {
        return new QwenVLOutputMessageImpl(jsonObject);
    }

    default QwenVLRole getRole() {
        String role = readString("role");
        if (role == null) return null;
        return QwenVLRole.valueOf(role);
    }

    default List<QwenVLMessageContentItem> getContent() {
        List<JsonObject> content = readJsonObjectArray("content");
        if(content == null) return null;
        return content.stream().map(QwenVLMessageContentItem::wrap).toList();
    }
}

