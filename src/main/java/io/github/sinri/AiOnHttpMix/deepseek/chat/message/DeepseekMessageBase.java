package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;

public interface DeepseekMessageBase<T> extends JsonifiableEntity<T>, SelfInterface<T> {
    default T setRole(DeepseekMessageInRequest.Role role) {
        toJsonObject().put("role", role.name());
        return getImplementation();
    }

    default T setContent(String content) {
        toJsonObject().put("content", content);
        return getImplementation();
    }
}
