package io.github.sinri.AiOnHttpMix.test.volces;

import io.github.sinri.AiOnHttpMix.test.BaseUnitTest;
import io.github.sinri.AiOnHttpMix.volces.core.VolcesServiceMeta;
import org.junit.Before;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class VolcesTestCore extends BaseUnitTest {
    private VolcesServiceMeta serviceMeta;

    public VolcesServiceMeta getServiceMeta() {
        return serviceMeta;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        String apiKey = Keel.config("volces.doubao-pro-128k.apiKey");
        String model = Keel.config("volces.doubao-pro-128k.model");

        serviceMeta = new VolcesServiceMeta(apiKey, model);
    }
}
