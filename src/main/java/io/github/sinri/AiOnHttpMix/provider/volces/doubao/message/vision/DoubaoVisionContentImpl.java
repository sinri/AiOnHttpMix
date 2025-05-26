package io.github.sinri.AiOnHttpMix.provider.volces.doubao.message.vision;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

class DoubaoVisionContentImpl extends JsonifiableEntityImpl<DoubaoVisionContent> implements DoubaoVisionContent {
    public DoubaoVisionContentImpl(JsonObject jsonObject) {
        super(jsonObject);
    }

    public DoubaoVisionContentImpl() {
        super();
    }

    @Nonnull
    @Override
    public DoubaoVisionContent getImplementation() {
        return this;
    }
}
