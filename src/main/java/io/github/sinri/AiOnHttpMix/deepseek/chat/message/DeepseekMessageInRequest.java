package io.github.sinri.AiOnHttpMix.deepseek.chat.message;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface DeepseekMessageInRequest
        extends JsonifiableEntity<DeepseekMessageInRequest>,
        DeepseekMessageInRequestUserMixin<DeepseekMessageInRequest>,
        DeepseekMessageInRequestToolMixin<DeepseekMessageInRequest>,
        DeepseekMessageInRequestAssistantMixin<DeepseekMessageInRequest> {
    static DeepseekMessageInRequest create() {
        return new DeepseekMessageInRequestImpl();
    }

    static DeepseekMessageInRequest wrap(@NotNull JsonObject jsonObject) {
        return new DeepseekMessageInRequestImpl(jsonObject);
    }


    enum Role {
        system,
        user,
        assistant,
        tool,
    }
}
