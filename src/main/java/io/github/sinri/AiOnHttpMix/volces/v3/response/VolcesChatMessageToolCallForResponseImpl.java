package io.github.sinri.AiOnHttpMix.volces.v3.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class VolcesChatMessageToolCallForResponseImpl extends UnmodifiableJsonifiableEntityImpl implements VolcesChatMessageToolCallForResponse {

    public VolcesChatMessageToolCallForResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }
}
