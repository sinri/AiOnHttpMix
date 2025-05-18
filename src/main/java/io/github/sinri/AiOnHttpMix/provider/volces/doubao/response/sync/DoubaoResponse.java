package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

public interface DoubaoResponse extends JsonifiableEntity<DoubaoResponse> {

    static DoubaoResponse create() {
        return new DoubaoResponseImpl();
    }

    static DoubaoResponse wrap(JsonObject jsonObject) {
        return new DoubaoResponseImpl(jsonObject);
    }
}
