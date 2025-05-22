package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;


class DoubaoResponseImpl extends UnmodifiableJsonifiableEntityImpl implements DoubaoResponse {

    public DoubaoResponseImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

}
