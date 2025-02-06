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

    protected String getServiceName() {
        return "doubao-pro-128k";
    }

    protected VolcesServiceMeta buildServiceMeta() {
        String serviceName = getServiceName();
        String apiKey = Keel.config("volces." + serviceName + ".apiKey");
        String model = Keel.config("volces." + serviceName + ".model");

        return new VolcesServiceMeta(apiKey, model);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        serviceMeta = buildServiceMeta();
    }
}
