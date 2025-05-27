package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageKit;
import io.github.sinri.keel.facade.tesuto.unit.KeelUnitTest;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractMirageUnitTest extends KeelUnitTest {
    private final MirageKit mirageKit;

    public AbstractMirageUnitTest() {
        super();

        var domain = Keel.config("mirage.domain");
        var clientCode = Keel.config("mirage.client_code");
        var clientSecret = Keel.config("mirage.client_secret");

        mirageKit = new MirageKit(domain, clientCode, clientSecret);
    }

    protected final MirageKit getMirageKit() {
        return mirageKit;
    }
}
