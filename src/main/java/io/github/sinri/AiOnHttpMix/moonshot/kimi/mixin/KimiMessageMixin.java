package io.github.sinri.AiOnHttpMix.moonshot.kimi.mixin;

import io.github.sinri.AiOnHttpMix.moonshot.kimi.KimiKit;
import io.github.sinri.keel.core.SelfInterface;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import org.jetbrains.annotations.Nullable;

public interface KimiMessageMixin<E> extends JsonifiableEntity<E>, SelfInterface<E> {
    @Nullable
    default KimiKit.Role getRole() {
        return KimiKit.Role.valueOf(readString("role"));
    }

    default E setRole(KimiKit.Role role) {
        toJsonObject().put("role", role.name());
        return getImplementation();
    }

    @Nullable
    default String getContent() {
        return readString("content");
    }

    default E setContent(String content) {
        toJsonObject().put("content", content);
        return getImplementation();
    }

    @Nullable
    default JsonArray getToolCalls() {
        // todo
        return readJsonArray("tool_calls");
    }
}
