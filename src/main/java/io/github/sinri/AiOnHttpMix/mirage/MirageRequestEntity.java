package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.keel.core.json.JsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageRequestEntity extends JsonifiableEntityImpl<MirageRequestEntity> {

    public MirageRequestEntity() {
        super();
    }

    public MirageRequestEntity(@Nonnull JsonObject jsonObject) {
        super(jsonObject);
    }

    public static MirageRequestEntity fromNayCodeEncodedString(@Nonnull String nyacode) {
        return new MirageRequestEntity(new JsonObject(Keel.stringHelper().decodeFromNyaCode(nyacode)));
    }

    @Nonnull
    @Override
    public MirageRequestEntity getImplementation() {
        return this;
    }
}
