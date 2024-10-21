package io.github.sinri.AiOnHttpMix.test.dashscope;

import io.github.sinri.AiOnHttpMix.AigcMix;
import io.github.sinri.AiOnHttpMix.dashscope.core.DashscopeServiceMeta;
import io.github.sinri.AiOnHttpMix.test.BaseUnitTest;
import io.github.sinri.keel.logger.KeelLogLevel;
import org.junit.Before;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class DashscopeTestCore extends BaseUnitTest {
    private DashscopeServiceMeta serviceMeta;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        String dashscopeApiKey = Keel.config("dashscope.api_key");

        this.serviceMeta = new DashscopeServiceMeta(dashscopeApiKey);

        AigcMix.enableVerboseLogger(KeelLogLevel.DEBUG);
    }

    protected DashscopeServiceMeta getServiceMeta() {
        return serviceMeta;
    }
}
