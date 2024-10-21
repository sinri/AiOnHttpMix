package io.github.sinri.AiOnHttpMix.test.mirage;

import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.test.BaseUnitTest;
import org.junit.Before;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class MirageTestBase extends BaseUnitTest {
    private MirageSDK mirageSDK;

    public MirageSDK getMirageSDK() {
        return mirageSDK;
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        var domain = Keel.config("mirage.domain");
        var clientCode = Keel.config("mirage.client_code");
        var clientSecret = Keel.config("mirage.client_secret");

        mirageSDK = new MirageSDK(domain, clientCode, clientSecret);
    }
}
