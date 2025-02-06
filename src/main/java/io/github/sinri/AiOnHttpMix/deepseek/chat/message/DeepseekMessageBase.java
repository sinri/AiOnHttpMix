package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface DeepseekMessageBase<T> extends JsonifiableEntity<T>, SelfInterface<T> {
    default T setRole(DeepseekRole role) {
        toJsonObject().put("role", role.name());
        return getImplementation();
    }

    default DeepseekRole getRole() {
        return DeepseekRole.valueOf(readString("role"));
    }

    default T setContent(String content) {
        toJsonObject().put("content", content);
        return getImplementation();
    }

    default String getContent() {
        return readString("content");
    }
}
