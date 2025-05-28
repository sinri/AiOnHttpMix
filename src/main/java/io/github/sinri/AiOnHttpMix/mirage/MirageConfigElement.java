package io.github.sinri.AiOnHttpMix.mirage;

import io.github.sinri.keel.facade.configuration.KeelConfigElement;

import javax.annotation.Nonnull;
import java.util.List;

public class MirageConfigElement extends KeelConfigElement {
    public MirageConfigElement(@Nonnull KeelConfigElement another) {
        super(another);
    }

    public String getDomain() {
        return readString(List.of("domain"));
    }

    public String getClientCode() {
        return readString(List.of("client_code"));
    }

    public String getClientSecret() {
        return readString(List.of("client_secret"));
    }
}
