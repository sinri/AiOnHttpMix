package io.github.sinri.AiOnHttpMix.volces.v3.request;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class VolcesChatFunctionCallForRequestImpl extends UnmodifiableJsonifiableEntityImpl implements VolcesChatFunctionCallForRequest {
    public VolcesChatFunctionCallForRequestImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
