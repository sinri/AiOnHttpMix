package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.vl;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface QwenVLChatOutputMessageMixin extends UnmodifiableJsonifiableEntity {
    default QwenVLChatRole getRole() {
        String role = readString("role");
        if (role == null) return null;
        return QwenVLChatRole.valueOf(role);
    }

    default List<QwenVLChatMessageContentItem> getContent() {
        List<JsonObject> content = readJsonObjectArray("content");
        if(content == null) return null;
        return content.stream().map(QwenVLChatMessageContentItem::wrap).toList();
    }
}

