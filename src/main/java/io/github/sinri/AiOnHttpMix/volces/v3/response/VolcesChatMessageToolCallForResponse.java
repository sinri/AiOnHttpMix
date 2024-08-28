package io.github.sinri.AiOnHttpMix.volces.v3.response;

import io.github.sinri.AiOnHttpMix.volces.v3.tool.SharedMessageToolCall;
import io.vertx.core.json.JsonObject;

public interface VolcesChatMessageToolCallForResponse extends SharedMessageToolCall {
    static VolcesChatMessageToolCallForResponse wrap(JsonObject jsonObject) {
        return new VolcesChatMessageToolCallForResponseImpl(jsonObject);
    }
}
