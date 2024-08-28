package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.github.sinri.AiOnHttpMix.volces.v3.tool.SharedFunctionCall;
import io.vertx.core.json.JsonObject;


public interface VolcesChatFunctionCallForRequest extends SharedFunctionCall {
    static VolcesChatFunctionCallForRequest wrap(JsonObject jsonObject) {
        return new VolcesChatFunctionCallForRequestImpl(jsonObject);
    }

}
