package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.github.sinri.AiOnHttpMix.volces.v3.VolcesChatRole;
import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface VolcesChatMessageForRequest extends JsonifiableEntity<VolcesChatMessageForRequest> {
    static VolcesChatMessageForRequest create() {
        return new VolcesChatMessageForRequestImpl();
    }

    static VolcesChatMessageForRequest wrap(JsonObject jsonObject) {
        return new VolcesChatMessageForRequestImpl(jsonObject);
    }

    default VolcesChatRole getRole() {
        return VolcesChatRole.valueOf(readString("role"));
    }

    default VolcesChatMessageForRequest setRole(VolcesChatRole role) {
        this.toJsonObject().put("role", role.name());
        return this;
    }

    @Nullable
    default String getContent() {
        return readString("content");
    }

    default VolcesChatMessageForRequest setContent(@Nullable String content) {
        this.toJsonObject().put("content", content);
        return this;
    }

    @Nullable
    default String getToolCallId() {
        return readString("tool_call_id");
    }

    default VolcesChatMessageForRequest setToolCallId(@Nullable String toolCallId) {
        this.toJsonObject().put("tool_call_id", toolCallId);
        return this;
    }

    default VolcesChatMessageForRequest setToolCall(VolcesChatMessageToolCallForRequest toolCallParam) {
        JsonArray array = this.toJsonObject().getJsonArray("tool_calls");
        if (array == null) {
            array = new JsonArray();
            this.toJsonObject().put("tool_calls", array);
        }
        array.add(toolCallParam.toJsonObject());
        return this;
    }

}
