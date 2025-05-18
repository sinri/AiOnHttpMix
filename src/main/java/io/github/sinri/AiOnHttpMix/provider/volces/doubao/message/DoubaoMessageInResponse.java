package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message;

import io.vertx.core.json.JsonObject;

public interface DoubaoMessageInResponse extends DoubaoMessage {

    static DoubaoMessageInResponse wrap(JsonObject jsonObject) {
        return new DoubaoMessageImpl(jsonObject);
    }
}
