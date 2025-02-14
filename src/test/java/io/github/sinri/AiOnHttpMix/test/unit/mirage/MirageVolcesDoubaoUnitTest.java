package io.github.sinri.AiOnHttpMix.test.unit.mirage;

import io.github.sinri.AiOnHttpMix.test.unit.core.TestFailed;
import io.github.sinri.AiOnHttpMix.test.unit.core.TestPassed;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import org.junit.Test;

public class MirageVolcesDoubaoUnitTest extends AnyMirageUnitTest {

    @Override
    public String getModel() {
        return SupportedModel.Doubao.name();
    }

    @Override
    public String getService() {
        return "doubao-pro-128k";
    }

    @Test
    @TestFailed(time = "2025-02-14", note = "豆包调用FC的触发门槛有点高，不一定会触发。")
    @Override
    public void testSyncWithFC() {
        super.testSyncWithFC();
    }

    @Test
    @TestPassed(time = "2025-02-14")
    @Override
    public void testStreamWithFC() {
        super.testStreamWithFC();
    }

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
