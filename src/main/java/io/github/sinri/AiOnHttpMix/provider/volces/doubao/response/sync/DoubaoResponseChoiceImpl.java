package io.github.sinri.AiOnHttpMix.provider.volces.doubao.response.sync;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class DoubaoResponseChoiceImpl extends UnmodifiableJsonifiableEntityImpl implements DoubaoResponseChoice {
    public DoubaoResponseChoiceImpl(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }
}
