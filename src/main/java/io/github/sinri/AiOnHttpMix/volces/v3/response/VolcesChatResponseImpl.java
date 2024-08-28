package io.github.sinri.AiOnHttpMix.volces.v3.response;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

class VolcesChatResponseImpl extends UnmodifiableJsonifiableEntityImpl implements VolcesChatResponse {

    public VolcesChatResponseImpl(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

}
