package io.github.sinri.AiOnHttpMix.dashscope.qwen.mixin.vl;

import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;

public interface QwenVLChatInputMessageMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {

    default QwenVLChatRole getRole() {
        return QwenVLChatRole.valueOf(readString("role"));
    }

    default E setRole(QwenVLChatRole role) {
        toJsonObject().put("role", role.name());
        return getImplementation();
    }

    default E addContentItem(QwenVLChatMessageContentItem content) {
        JsonArray jsonArray = toJsonObject().getJsonArray("content");
        if (jsonArray == null) {
            jsonArray = new JsonArray();
            toJsonObject().put("content", jsonArray);
        }
        jsonArray.add(content.toJsonObject());
        return getImplementation();
    }


}
