package io.github.sinri.AiOnHttpMix.test.unit.anyllm.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.test.unit.anyllm.AbstractAnyLLMUnitTest;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractAnyLLMMirageUnitTest extends AbstractAnyLLMUnitTest {

    protected MirageSDK generateMirageSDK() {
        var domain = Keel.config("mirage.domain");
        var clientCode = Keel.config("mirage.client_code");
        var clientSecret = Keel.config("mirage.client_secret");

        return new MirageSDK(domain, clientCode, clientSecret);
    }
}
