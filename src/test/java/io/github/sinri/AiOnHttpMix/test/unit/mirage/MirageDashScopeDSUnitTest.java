package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

public class MirageDashScopeDSUnitTest extends AnyMirageUnitTest {

    @Override
    public String getModel() {
        return SupportedModel.DeepSeekChatOnDashScope.name();
    }

    @Override
    public String getService() {
        return null;
    }

    //    @Test
    //    @TestPassed(time = "2025-02-14")
    //    @Override
    //    public void testSyncWithFC() {
    //        super.testSyncWithFC();
    //    }

    //    @Test
    //    @TestPassed(time = "2025-02-14")
    //    @Override
    //    public void testStreamWithFC() {
    //        super.testStreamWithFC();
    //    }

    @Test
    @TestPassed(time = "2025-02-14")
    @Override
    public void testSyncWithoutFC() {
        super.testSyncWithoutFC();
    }

    @Test
    @TestPassed(time = "2025-02-14")
    @Override
    public void testStreamWithoutFC() {
        super.testStreamWithoutFC();
    }
}
